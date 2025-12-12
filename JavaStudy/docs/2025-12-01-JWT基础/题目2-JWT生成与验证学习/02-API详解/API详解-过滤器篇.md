# API详解：过滤器篇

## 📖 目录

- [1. OncePerRequestFilter API详解](#1-onceperrequestfilter-api详解)
- [2. HttpServletRequest API详解](#2-httpservletrequest-api详解)
- [3. FilterChain API详解](#3-filterchain-api详解)

---

## 1. OncePerRequestFilter API详解

### 1.1 API基本信息

- **类名**：`org.springframework.web.filter.OncePerRequestFilter`
- **包路径**：`org.springframework.web.filter`
- **父类**：`javax.servlet.Filter`
- **版本要求**：Spring Framework 2.0+

### 1.2 作用说明

**OncePerRequestFilter解决了什么问题**？

在普通的Servlet Filter中，如果请求被forward或include，过滤器可能会被执行多次：

```
请求 → Filter → Controller → forward → 另一个Controller → Filter再次执行 ❌
```

这会导致：
- 性能问题：重复执行不必要的逻辑
- 逻辑错误：JWT验证执行两次，可能导致问题
- 资源浪费：重复验证Token

**OncePerRequestFilter的解决方案**：

使用请求属性（Request Attribute）标记，确保每个请求只执行一次：

```java
request.setAttribute(ALREADY_FILTERED_SUFFIX, Boolean.TRUE);
```

**为什么在JWT场景中使用它**：

- ✅ 避免重复验证Token（浪费性能）
- ✅ 避免重复设置SecurityContext（可能导致问题）
- ✅ 提高性能（每个请求只验证一次）

### 1.3 使用场景

**适用场景**：
- 认证过滤器（如JWT过滤器）
- 日志记录过滤器（避免重复记录）
- 请求头设置过滤器

**不适用场景**：
- 需要在forward时也执行的过滤器
- 需要多次执行的过滤器（很少见）

### 1.4 API详解

#### 核心方法：doFilterInternal()

**方法签名**：

```java
protected abstract void doFilterInternal(
    HttpServletRequest request,
    HttpServletResponse response,
    FilterChain filterChain
) throws ServletException, IOException;
```

**参数说明**：

| 参数 | 类型 | 说明 |
|------|------|------|
| `request` | `HttpServletRequest` | HTTP请求对象，包含请求头、参数等信息 |
| `response` | `HttpServletResponse` | HTTP响应对象，用于设置响应信息 |
| `filterChain` | `FilterChain` | 过滤器链，用于继续传递请求 |

**返回值**：无

**异常说明**：

- `ServletException`：Servlet相关异常
- `IOException`：IO相关异常

**注意**：这是抽象方法，子类必须实现。

#### 可选方法：shouldNotFilter()

**方法签名**：

```java
protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException;
```

**作用**：判断当前请求是否应该跳过过滤器

**返回值**：
- `true`：跳过过滤器，不执行
- `false`：执行过滤器（默认）

**使用场景**：

```java
@Override
protected boolean shouldNotFilter(HttpServletRequest request) {
    String path = request.getRequestURI();
    // 跳过公开路径，不进行JWT验证
    return path.startsWith("/public/") || path.startsWith("/login");
}
```

**典型应用**：
- 跳过静态资源（CSS、JS、图片）
- 跳过公开接口（登录、注册）
- 跳过健康检查接口

### 1.5 完整案例代码

#### 案例1：基础使用（JWT过滤器）

```java
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        
        // 1. 从请求头提取Token
        String authHeader = request.getHeader("Authorization");
        
        // 2. 验证Token格式
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);  // 去掉"Bearer "前缀
            
            // 3. 验证Token
            String username = jwtUtil.getUsernameFromToken(token);
            
            // 4. 如果Token有效，设置认证信息
            if (username != null && !username.startsWith("Token")) {
                // 创建认证对象
                Authentication auth = new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    getAuthorities(token)
                );
                
                // 设置到SecurityContext
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        
        // 5. 继续执行过滤器链
        filterChain.doFilter(request, response);
    }
    
    private List<GrantedAuthority> getAuthorities(String token) {
        // 从Token中提取权限信息
        // TODO: 实现权限提取逻辑
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
    }
}
```

**代码说明**：
- 继承`OncePerRequestFilter`，实现`doFilterInternal()`方法
- 从请求头提取Token
- 验证Token并设置认证信息
- 调用`filterChain.doFilter()`继续过滤器链

#### 案例2：跳过特定路径

```java
@Override
protected boolean shouldNotFilter(HttpServletRequest request) {
    String path = request.getRequestURI();
    
    // 跳过以下路径，不进行JWT验证
    List<String> excludedPaths = Arrays.asList(
        "/api/login",
        "/api/register",
        "/api/public",
        "/swagger-ui",
        "/v3/api-docs"
    );
    
    return excludedPaths.stream().anyMatch(path::startsWith);
}
```

**使用场景**：
- 登录接口不需要Token
- 公开API不需要Token
- 静态资源不需要Token

#### 案例3：企业级实现（包含日志和异常处理）

```java
@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // 跳过OPTIONS请求（CORS预检请求）
        if ("OPTIONS".equals(request.getMethod())) {
            return true;
        }
        
        // 跳过公开路径
        String path = request.getRequestURI();
        return path.startsWith("/public/") || path.startsWith("/login");
    }
    
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        
        try {
            // 提取Token
            String token = extractToken(request);
            
            if (token != null) {
                // 验证Token
                String username = jwtUtil.getUsernameFromToken(token);
                
                if (isValidUsername(username)) {
                    // 设置认证信息
                    setAuthentication(username, token);
                    
                    log.debug("JWT认证成功: username={}, uri={}", 
                        username, request.getRequestURI());
                } else {
                    log.warn("JWT认证失败: token无效, uri={}", 
                        request.getRequestURI());
                }
            }
        } catch (Exception e) {
            // 记录日志，但不中断过滤器链
            log.error("JWT过滤器执行异常: {}", e.getMessage(), e);
            // 不抛出异常，让后续处理统一处理
        }
        
        // 继续执行过滤器链
        filterChain.doFilter(request, response);
    }
    
    private String extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
    
    private boolean isValidUsername(String username) {
        return username != null && !username.startsWith("Token");
    }
    
    private void setAuthentication(String username, String token) {
        List<GrantedAuthority> authorities = getAuthoritiesFromToken(token);
        Authentication auth = new UsernamePasswordAuthenticationToken(
            username,
            null,
            authorities
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
    
    private List<GrantedAuthority> getAuthoritiesFromToken(String token) {
        // 从Token中提取权限
        // TODO: 实现
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
    }
}
```

**企业级特性**：
- ✅ 跳过OPTIONS请求（CORS预检）
- ✅ 跳过公开路径
- ✅ 完整的异常处理
- ✅ 详细的日志记录
- ✅ 方法拆分，职责清晰

### 1.6 错误使用示例（对比学习）

#### ❌ 错误1：忘记调用filterChain.doFilter()

```java
@Override
protected void doFilterInternal(...) {
    // 验证Token...
    if (token有效) {
        // 设置认证信息
    }
    // ❌ 忘记调用 filterChain.doFilter()
    // 结果：请求被拦截，永远无法到达Controller
}
```

**正确做法**：
```java
// ✅ 必须调用，无论Token是否有效
filterChain.doFilter(request, response);
```

#### ❌ 错误2：在验证失败时抛出异常

```java
if (token无效) {
    throw new AuthenticationException("Token无效");  // ❌ 不要这样做
}
```

**正确做法**：
```java
// ✅ 不设置认证信息，继续执行过滤器链
// 让FilterSecurityInterceptor处理（返回401）
filterChain.doFilter(request, response);
```

#### ❌ 错误3：重复验证Token

```java
// ❌ 不使用OncePerRequestFilter，可能导致重复验证
public class JwtFilter implements Filter {
    // 可能在forward时重复执行
}
```

**正确做法**：
```java
// ✅ 继承OncePerRequestFilter，确保只执行一次
public class JwtFilter extends OncePerRequestFilter {
    // ...
}
```

### 1.7 注意事项

1. **必须调用filterChain.doFilter()**
   - 不调用会导致请求被拦截
   - 无论验证成功还是失败，都应该调用

2. **不要抛出异常**
   - 验证失败时，不设置认证信息即可
   - 让Spring Security统一处理异常

3. **线程安全**
   - SecurityContextHolder使用ThreadLocal，天然线程安全
   - 但要注意不要在过滤器中使用共享变量

4. **性能考虑**
   - 使用shouldNotFilter()跳过不需要验证的请求
   - 避免在过滤器中执行耗时操作

---

## 2. HttpServletRequest API详解

### 2.1 API基本信息

- **接口名**：`javax.servlet.http.HttpServletRequest`
- **包路径**：`javax.servlet.http`
- **继承关系**：`ServletRequest` ← `HttpServletRequest`
- **版本要求**：Servlet API 2.3+

### 2.2 作用说明

**HttpServletRequest提供了什么**？

封装了HTTP请求的所有信息，包括：
- 请求头（Headers）
- 请求参数（Parameters）
- 请求URI和路径
- 请求方法（GET、POST等）
- 客户端信息（IP、User-Agent等）

**在JWT场景中的作用**：
- 提取Authorization请求头
- 获取Token字符串
- 获取请求路径（用于判断是否需要验证）

### 2.3 使用场景

**在JWT过滤器中的使用**：

1. **提取Authorization头** - 获取Bearer Token
2. **获取请求路径** - 判断是否需要JWT验证
3. **获取请求方法** - 处理OPTIONS请求（CORS）

### 2.4 API详解

#### 核心方法：getHeader()

**方法签名**：

```java
String getHeader(String name);
```

**作用**：获取指定名称的请求头值

**参数说明**：

| 参数 | 类型 | 说明 |
|------|------|------|
| `name` | `String` | 请求头名称，如 "Authorization" |

**返回值**：
- 请求头的值（String）
- 如果不存在，返回`null`

**案例代码**：

```java
// 获取Authorization头
String authHeader = request.getHeader("Authorization");
// 结果示例: "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."

// 获取Content-Type头
String contentType = request.getHeader("Content-Type");
// 结果示例: "application/json"

// 不存在的头返回null
String customHeader = request.getHeader("Custom-Header");
// 结果: null
```

#### 核心方法：getRequestURI()

**方法签名**：

```java
String getRequestURI();
```

**作用**：获取请求URI（不包含查询参数）

**返回值示例**：
- `/api/user/info`
- `/api/login`
- `/public/health`

**案例代码**：

```java
String uri = request.getRequestURI();
// 判断是否需要JWT验证
if (uri.startsWith("/public/")) {
    // 跳过验证
}
```

#### 核心方法：getMethod()

**方法签名**：

```java
String getMethod();
```

**作用**：获取HTTP请求方法

**返回值**：
- `GET`、`POST`、`PUT`、`DELETE`、`OPTIONS`等

**案例代码**：

```java
// 跳过OPTIONS请求（CORS预检）
if ("OPTIONS".equals(request.getMethod())) {
    return true;  // 在shouldNotFilter中
}
```

#### 其他常用方法

**getParameter()** - 获取请求参数

```java
String username = request.getParameter("username");
```

**getRemoteAddr()** - 获取客户端IP

```java
String clientIp = request.getRemoteAddr();
```

**getContextPath()** - 获取上下文路径

```java
String contextPath = request.getContextPath();
```

### 2.5 完整案例代码

#### 案例1：提取Bearer Token

```java
private String extractToken(HttpServletRequest request) {
    // 1. 获取Authorization头
    String authHeader = request.getHeader("Authorization");
    
    // 2. 检查是否为null
    if (authHeader == null) {
        return null;
    }
    
    // 3. 检查是否有"Bearer "前缀
    if (!authHeader.startsWith("Bearer ")) {
        return null;  // 格式错误
    }
    
    // 4. 提取Token（去掉"Bearer "前缀，共7个字符）
    String token = authHeader.substring(7);
    
    // 5. 检查Token是否为空
    if (token == null || token.trim().isEmpty()) {
        return null;
    }
    
    return token.trim();
}
```

**完整实现（企业级）**：

```java
private String extractToken(HttpServletRequest request) {
    try {
        String authHeader = request.getHeader("Authorization");
        
        if (authHeader == null || authHeader.trim().isEmpty()) {
            log.debug("Authorization头不存在");
            return null;
        }
        
        // 去掉首尾空格
        authHeader = authHeader.trim();
        
        // 检查Bearer前缀（不区分大小写）
        String bearerPrefix = "Bearer ";
        if (!authHeader.toLowerCase().startsWith("bearer ")) {
            log.warn("Authorization头格式错误，缺少Bearer前缀: {}", authHeader);
            return null;
        }
        
        // 提取Token
        String token = authHeader.substring(bearerPrefix.length()).trim();
        
        if (token.isEmpty()) {
            log.warn("Token为空");
            return null;
        }
        
        return token;
    } catch (Exception e) {
        log.error("提取Token异常: {}", e.getMessage(), e);
        return null;
    }
}
```

**企业级特性**：
- ✅ 完整的null检查
- ✅ 不区分大小写的Bearer前缀检查
- ✅ 详细的日志记录
- ✅ 异常处理

#### 案例2：判断请求是否需要JWT验证

```java
@Override
protected boolean shouldNotFilter(HttpServletRequest request) {
    String uri = request.getRequestURI();
    String method = request.getMethod();
    
    // 跳过OPTIONS请求
    if ("OPTIONS".equals(method)) {
        return true;
    }
    
    // 跳过公开路径
    List<String> publicPaths = Arrays.asList(
        "/api/login",
        "/api/register",
        "/api/public",
        "/swagger-ui",
        "/v3/api-docs",
        "/actuator/health"
    );
    
    return publicPaths.stream().anyMatch(path -> uri.startsWith(path));
}
```

### 2.6 错误使用示例

#### ❌ 错误1：没有检查null

```java
String authHeader = request.getHeader("Authorization");
String token = authHeader.substring(7);  // ❌ 如果authHeader为null，会抛NPE
```

**正确做法**：
```java
String authHeader = request.getHeader("Authorization");
if (authHeader != null && authHeader.startsWith("Bearer ")) {
    String token = authHeader.substring(7);
}
```

#### ❌ 错误2：没有检查前缀

```java
String authHeader = request.getHeader("Authorization");
String token = authHeader.substring(7);  // ❌ 如果不是"Bearer "开头，会提取错误
```

**正确做法**：
```java
if (authHeader != null && authHeader.startsWith("Bearer ")) {
    String token = authHeader.substring(7);
}
```

---

## 3. FilterChain API详解

### 3.1 API基本信息

- **接口名**：`javax.servlet.FilterChain`
- **包路径**：`javax.servlet`
- **版本要求**：Servlet API 2.3+

### 3.2 作用说明

**FilterChain是什么**？

FilterChain代表过滤器链，负责按顺序调用链中的每个过滤器，最终到达目标资源（如Controller）。

**为什么需要FilterChain**：

- 多个过滤器按顺序执行
- 每个过滤器决定是否继续传递请求
- 如果不调用，请求就会被拦截

### 3.3 使用场景

**在JWT过滤器中的使用**：

- 验证Token后，继续传递请求
- 无论验证成功还是失败，都应该调用
- 让后续过滤器和Controller继续处理

### 3.4 API详解

#### 核心方法：doFilter()

**方法签名**：

```java
void doFilter(ServletRequest request, ServletResponse response) 
    throws IOException, ServletException;
```

**作用**：继续执行过滤器链中的下一个过滤器

**参数说明**：

| 参数 | 类型 | 说明 |
|------|------|------|
| `request` | `ServletRequest` | 请求对象（通常转换为HttpServletRequest） |
| `response` | `ServletResponse` | 响应对象（通常转换为HttpServletResponse） |

**返回值**：无

**异常说明**：
- `IOException`：IO相关异常
- `ServletException`：Servlet相关异常

**关键点**：
- **必须调用**：不调用则请求被拦截
- **无论成功失败都要调用**：即使验证失败也要调用
- **调用时机**：在完成当前过滤器的处理后调用

### 3.5 完整案例代码

#### 案例1：基础使用

```java
@Override
protected void doFilterInternal(...) {
    // 1. 处理请求（验证Token）
    String token = extractToken(request);
    if (token != null) {
        validateAndSetAuthentication(token);
    }
    
    // 2. 继续执行过滤器链
    filterChain.doFilter(request, response);
    
    // 3. 处理响应（如果有需要）
    // 注意：这里可以处理响应，但JWT过滤器通常不需要
}
```

#### 案例2：完整的JWT过滤器流程

```java
@Override
protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
) throws ServletException, IOException {
    
    try {
        // 步骤1：提取Token
        String token = extractToken(request);
        
        if (token != null) {
            // 步骤2：验证Token
            String username = jwtUtil.getUsernameFromToken(token);
            
            if (isValidUsername(username)) {
                // 步骤3：设置认证信息
                setAuthentication(username, token);
                log.debug("JWT认证成功: {}", username);
            } else {
                log.debug("JWT认证失败: Token无效");
            }
        } else {
            log.debug("未找到JWT Token");
        }
        
    } catch (Exception e) {
        // 记录日志，但不中断流程
        log.error("JWT过滤器异常: {}", e.getMessage());
    }
    
    // 步骤4：无论成功失败，都继续执行过滤器链
    // 这是关键！不调用会导致请求被拦截
    filterChain.doFilter(request, response);
}
```

### 3.6 错误使用示例

#### ❌ 错误1：验证失败时不调用doFilter()

```java
if (token有效) {
    setAuthentication(token);
    filterChain.doFilter(request, response);  // ✅ 只在成功时调用
} else {
    // ❌ 失败时不调用，请求被拦截
    response.sendError(401);
}
```

**正确做法**：
```java
if (token有效) {
    setAuthentication(token);
}
// ✅ 无论成功失败，都要调用
filterChain.doFilter(request, response);
```

#### ❌ 错误2：调用doFilter()后还修改响应

```java
filterChain.doFilter(request, response);
// ❌ 在doFilter()之后修改响应可能导致问题
response.setHeader("Custom-Header", "value");
```

**说明**：虽然技术上可行，但在JWT过滤器中通常不需要这样做。

### 3.7 注意事项

1. **必须调用doFilter()**
   - 这是最关键的点
   - 不调用则请求永远无法到达Controller

2. **调用时机**
   - 在完成当前过滤器处理后调用
   - 不要在try-catch中遗漏调用

3. **参数传递**
   - 直接传递request和response
   - 不需要修改或包装

---

## 📝 总结

### 关键API记忆

1. **OncePerRequestFilter.doFilterInternal()** - 核心方法，实现过滤逻辑
2. **OncePerRequestFilter.shouldNotFilter()** - 可选方法，跳过特定请求
3. **HttpServletRequest.getHeader()** - 获取请求头
4. **FilterChain.doFilter()** - 继续执行过滤器链（必须调用）

### 最佳实践

1. ✅ 继承OncePerRequestFilter，确保只执行一次
2. ✅ 使用shouldNotFilter()跳过不需要验证的请求
3. ✅ 无论验证成功失败，都要调用filterChain.doFilter()
4. ✅ 不要抛出异常，让Spring Security统一处理
5. ✅ 完整的null检查和异常处理

### 下一步学习

掌握过滤器API后，接下来应该学习：

- [API详解-SpringSecurity篇](./API详解-SpringSecurity篇.md) - 学习SecurityContext等API
- [完整学习指南](../01-学习指南/完整学习指南.md) - 开始动手实现

---

*文档更新时间：2025年12月2日*
