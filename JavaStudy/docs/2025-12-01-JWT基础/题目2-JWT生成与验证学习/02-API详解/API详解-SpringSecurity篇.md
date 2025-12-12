# API详解：Spring Security篇

## 📖 目录

- [1. SecurityContextHolder API详解](#1-securitycontextholder-api详解)
- [2. UsernamePasswordAuthenticationToken API详解](#2-usernamepasswordauthenticationtoken-api详解)
- [3. HttpSecurity API详解](#3-httpsecurity-api详解)
- [4. Authentication API详解](#4-authentication-api详解)
- [5. GrantedAuthority API详解](#5-grantedauthority-api详解)

---

## 1. SecurityContextHolder API详解

### 1.1 API基本信息

- **类名**：`org.springframework.security.core.context.SecurityContextHolder`
- **包路径**：`org.springframework.security.core.context`
- **版本要求**：Spring Security 3.0+

### 1.2 作用说明

**SecurityContextHolder解决了什么问题**？

在Web应用中，每个请求都需要知道"当前用户是谁"，但HTTP是无状态的协议。为了在请求处理过程中随时获取当前认证用户的信息，Spring Security提供了SecurityContextHolder。

**为什么需要SecurityContextHolder**：

- ✅ **全局访问**：在应用的任何地方都可以获取当前用户
- ✅ **线程安全**：每个请求线程有独立的认证信息
- ✅ **自动管理**：请求开始时创建，请求结束时清理
- ✅ **无状态支持**：JWT等无状态认证的理想解决方案

**不使用会有什么后果**：
- 无法在Service层获取当前用户信息
- 需要手动在方法间传递用户信息
- 代码耦合度高，难以维护

### 1.3 使用场景

**典型使用场景**：

1. **Controller中获取用户信息**
   ```java
   @GetMapping("/user/info")
   public Result<User> getUserInfo() {
       String username = SecurityContextHolder.getContext()
           .getAuthentication().getName();
       // 获取用户信息...
   }
   ```

2. **Service中获取当前用户**
   ```java
   @Service
   public class UserService {
       public void updateUser(User user) {
           String currentUsername = SecurityContextHolder.getContext()
               .getAuthentication().getName();
           // 记录操作日志...
       }
   }
   ```

3. **权限检查**
   ```java
   public boolean hasPermission(String permission) {
       Authentication auth = SecurityContextHolder.getContext().getAuthentication();
       return auth.getAuthorities().stream()
           .anyMatch(granted -> granted.getAuthority().equals(permission));
   }
   ```

### 1.4 API详解

#### 核心方法：getContext()

**方法签名**：
```java
public static SecurityContext getContext()
```

**作用**：获取当前请求的SecurityContext

**返回值**：
- `SecurityContext`：当前请求的安全上下文
- 如果不存在，会自动创建一个新的SecurityContext

**使用示例**：
```java
// 获取SecurityContext
SecurityContext context = SecurityContextHolder.getContext();

// 获取Authentication
Authentication auth = context.getAuthentication();

// 获取用户名
String username = auth.getName();
```

#### 静态方法：setContext()

**方法签名**：
```java
public static void setContext(SecurityContext context)
```

**作用**：设置当前请求的SecurityContext

**参数说明**：

| 参数 | 类型 | 说明 |
|------|------|------|
| `context` | `SecurityContext` | 要设置的安全上下文 |

**使用场景**：
通常在认证过滤器中使用，设置认证成功的用户信息。

#### 静态方法：clearContext()

**方法签名**：
```java
public static void clearContext()
```

**作用**：清除当前请求的SecurityContext

**使用场景**：
- 手动登出
- 测试环境中清理认证信息
- 通常不需要手动调用，Spring Security会自动清理

### 1.5 存储策略详解

#### MODE_THREADLOCAL（默认）

**特点**：
- 使用ThreadLocal存储
- 每个线程独立的安全上下文
- 请求结束时自动清理
- 线程安全

**适用场景**：
- Web应用（最常用）
- 同步方法调用

#### MODE_INHERITABLETHREADLOCAL

**特点**：
- 子线程可以继承父线程的SecurityContext
- 适用于异步任务

**使用场景**：
```java
// 设置存储策略
SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);

new Thread(() -> {
    // 子线程可以获取父线程的SecurityContext
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
}).start();
```

#### MODE_GLOBAL

**特点**：
- 全局共享一个SecurityContext
- 不推荐使用，不安全

### 1.6 完整案例代码

#### 案例1：在JWT过滤器中设置认证信息

```java
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        
        String token = extractToken(request);
        
        if (token != null) {
            String username = jwtUtil.getUsernameFromToken(token);
            
            if (username != null && !username.startsWith("Token")) {
                // 创建权限列表
                List<GrantedAuthority> authorities = Arrays.asList(
                    new SimpleGrantedAuthority("ROLE_USER")
                );
                
                // 创建认证对象
                Authentication auth = new UsernamePasswordAuthenticationToken(
                    username,
                    null,  // JWT中不需要密码
                    authorities
                );
                
                // ✅ 设置到SecurityContextHolder
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        
        filterChain.doFilter(request, response);
    }
}
```

**代码说明**：
- 验证Token成功后，创建Authentication对象
- 使用SecurityContextHolder.getContext().setAuthentication()设置认证信息
- 设置后，后续代码都可以通过SecurityContextHolder获取用户信息

#### 案例2：在Controller中获取用户信息

```java
@RestController
@RequestMapping("/api/user")
public class UserController {
    
    @GetMapping("/info")
    public Result<Map<String, Object>> getUserInfo() {
        // ✅ 获取当前认证信息
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        if (auth == null || !auth.isAuthenticated()) {
            return Result.fail(401, "未登录");
        }
        
        // 获取用户名
        String username = auth.getName();
        
        // 获取权限信息
        List<String> authorities = auth.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList());
        
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("username", username);
        userInfo.put("authorities", authorities);
        userInfo.put("authenticated", auth.isAuthenticated());
        
        return Result.success(userInfo);
    }
}
```

#### 案例3：在Service中获取当前用户

```java
@Service
public class AuditService {
    
    public void logOperation(String operation, String targetId) {
        // ✅ 获取当前操作用户
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        String username = "anonymous";
        if (auth != null && auth.isAuthenticated()) {
            username = auth.getName();
        }
        
        // 记录操作日志
        log.info("用户[{}]执行操作[{}]，目标ID[{}]", username, operation, targetId);
    }
}
```

#### 案例4：异步方法中的认证信息传递

```java
@Service
public class AsyncService {
    
    @Async
    public void processAsync() {
        // 默认情况下，异步方法中获取不到SecurityContext
        
        // 解决方案1：手动传递
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        processWithAuth(auth);
    }
    
    @Async
    public void processWithAuth(Authentication auth) {
        // 在异步方法中设置认证信息
        SecurityContextHolder.getContext().setAuthentication(auth);
        
        // 现在可以正常获取用户信息
        String username = SecurityContextHolder.getContext()
            .getAuthentication().getName();
    }
}
```

### 1.7 注意事项

1. **线程安全**
   - SecurityContextHolder使用ThreadLocal，天然线程安全
   - 但异步方法中需要手动传递认证信息

2. **空值检查**
   ```java
   // ✅ 正确：总是检查null和isAuthenticated
   Authentication auth = SecurityContextHolder.getContext().getAuthentication();
   if (auth != null && auth.isAuthenticated()) {
       String username = auth.getName();
   }
   ```

3. **存储策略**
   - 生产环境使用默认的MODE_THREADLOCAL
   - 特殊情况（如异步任务）使用MODE_INHERITABLETHREADLOCAL

4. **性能考虑**
   - SecurityContextHolder.getContext()是轻量级操作
   - 但不要在循环中频繁调用

---

## 2. UsernamePasswordAuthenticationToken API详解

### 2.1 API基本信息

- **类名**：`org.springframework.security.authentication.UsernamePasswordAuthenticationToken`
- **包路径**：`org.springframework.security.authentication`
- **继承关系**：`AbstractAuthenticationToken` ← `UsernamePasswordAuthenticationToken`
- **版本要求**：Spring Security 3.0+

### 2.2 作用说明

**UsernamePasswordAuthenticationToken解决了什么问题**？

在Spring Security中，需要一个标准的方式来表示用户的认证信息。UsernamePasswordAuthenticationToken提供了一种标准的数据结构来封装：

- **谁在访问**（用户名）
- **凭证是什么**（密码或其他凭证）
- **有什么权限**（角色和权限列表）

**为什么需要它**：
- ✅ **标准化**：统一的认证信息表示方式
- ✅ **类型安全**：强类型的认证对象
- ✅ **信息完整**：包含认证所需的所有信息
- ✅ **框架集成**：Spring Security原生支持

### 2.3 使用场景

**典型使用场景**：

1. **表单登录认证**
   ```java
   // 用户提交用户名密码后
   Authentication auth = new UsernamePasswordAuthenticationToken(
       username, password
   );
   // 由AuthenticationManager验证
   ```

2. **JWT Token认证**
   ```java
   // 从Token中提取信息后
   Authentication auth = new UsernamePasswordAuthenticationToken(
       username, null, authorities
   );
   // 直接设置到SecurityContext
   ```

3. **自定义认证**
   ```java
   // 第三方系统认证后
   Authentication auth = new UsernamePasswordAuthenticationToken(
       userId, token, roles
   );
   ```

### 2.4 API详解

#### 构造函数详解

**构造函数1：未认证状态**

```java
public UsernamePasswordAuthenticationToken(Object principal, Object credentials)
```

**参数说明**：

| 参数 | 类型 | 说明 |
|------|------|------|
| `principal` | `Object` | 主体（通常是用户名） |
| `credentials` | `Object` | 凭证（通常是密码） |

**特点**：
- `isAuthenticated() = false`
- 用于表示待验证的认证请求

**使用场景**：
```java
// 用户登录时，还未验证密码
Authentication authRequest = new UsernamePasswordAuthenticationToken(
    "admin", "password123"
);
```

**构造函数2：已认证状态**

```java
public UsernamePasswordAuthenticationToken(Object principal, Object credentials, 
                                         Collection<? extends GrantedAuthority> authorities)
```

**参数说明**：

| 参数 | 类型 | 说明 |
|------|------|------|
| `principal` | `Object` | 主体（用户名或UserDetails对象） |
| `credentials` | `Object` | 凭证（通常为null，安全考虑） |
| `authorities` | `Collection<? extends GrantedAuthority>` | 权限列表 |

**特点**：
- `isAuthenticated() = true`
- 用于表示已验证的认证信息

**使用场景**：
```java
// JWT验证成功后
Authentication auth = new UsernamePasswordAuthenticationToken(
    username,
    null,  // JWT中不需要密码
    authorities
);
auth.setAuthenticated(true);
```

#### 核心方法：setAuthenticated()

**方法签名**：
```java
public void setAuthenticated(boolean isAuthenticated)
```

**作用**：设置认证状态

**参数说明**：
- `true`：表示已认证
- `false`：表示未认证

**重要**：这个方法只能由框架内部调用，普通代码不能随意设置。

### 2.5 完整案例代码

#### 案例1：JWT过滤器中的使用

```java
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        
        String token = extractToken(request);
        
        if (token != null) {
            // 验证Token并获取用户信息
            String username = validateTokenAndGetUsername(token);
            List<GrantedAuthority> authorities = getAuthorities(token);
            
            if (username != null) {
                // ✅ 创建已认证的UsernamePasswordAuthenticationToken
                UsernamePasswordAuthenticationToken auth = 
                    new UsernamePasswordAuthenticationToken(
                        username,           // principal（用户名）
                        null,              // credentials（JWT不需要密码）
                        authorities        // authorities（权限列表）
                    );
                
                // 设置为已认证状态
                auth.setAuthenticated(true);
                
                // 设置到SecurityContext
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        
        filterChain.doFilter(request, response);
    }
    
    private List<GrantedAuthority> getAuthorities(String token) {
        // 从Token中提取权限信息
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
    }
}
```

#### 案例2：不同构造函数的使用对比

```java
// 场景1：表单登录前的认证请求（未认证）
@PostMapping("/login")
public Result<?> login(@RequestBody LoginRequest request) {
    // 创建未认证的token，用于提交给AuthenticationManager
    Authentication authRequest = new UsernamePasswordAuthenticationToken(
        request.getUsername(),
        request.getPassword()
    );
    
    // AuthenticationManager会验证这个token
    Authentication authResult = authenticationManager.authenticate(authRequest);
    
    return Result.success(authResult);
}

// 场景2：JWT验证后的认证对象（已认证）
public Authentication createJwtAuthentication(String username, List<String> roles) {
    // 将角色字符串转换为GrantedAuthority
    List<GrantedAuthority> authorities = roles.stream()
        .map(SimpleGrantedAuthority::new)
        .collect(Collectors.toList());
    
    // ✅ 创建已认证的token
    UsernamePasswordAuthenticationToken auth = 
        new UsernamePasswordAuthenticationToken(
            username,       // principal
            null,          // credentials（JWT中为null）
            authorities    // authorities
        );
    
    auth.setAuthenticated(true); // 标记为已认证
    return auth;
}
```

#### 案例3：权限信息的处理

```java
public Authentication createAuthenticationWithRoles(String username, String roles) {
    // 解析角色字符串，如 "ROLE_USER,ROLE_ADMIN"
    List<GrantedAuthority> authorities = Arrays.stream(roles.split(","))
        .map(String::trim)
        .map(role -> new SimpleGrantedAuthority(role))
        .collect(Collectors.toList());
    
    UsernamePasswordAuthenticationToken auth = 
        new UsernamePasswordAuthenticationToken(username, null, authorities);
    auth.setAuthenticated(true);
    
    return auth;
}
```

### 2.6 注意事项

1. **构造函数选择**
   - 未认证状态：使用两个参数的构造函数
   - 已认证状态：使用三个参数的构造函数

2. **credentials处理**
   ```java
   // JWT认证中，credentials通常为null
   Authentication auth = new UsernamePasswordAuthenticationToken(
       username,
       null,  // 安全考虑，不存储密码
       authorities
   );
   ```

3. **权限列表**
   - 不能为空，至少包含一个权限
   - 可以使用`Collections.emptyList()`但不推荐

4. **线程安全**
   - UsernamePasswordAuthenticationToken是不可变的
   - 可以安全地在多线程间传递

---

## 3. HttpSecurity API详解

### 3.1 API基本信息

- **类名**：`org.springframework.security.config.annotation.web.builders.HttpSecurity`
- **包路径**：`org.springframework.security.config.annotation.web.builders`
- **版本要求**：Spring Security 3.2+

### 3.2 作用说明

**HttpSecurity解决了什么问题**？

Spring Security需要知道：
- 哪些请求需要认证？
- 哪些请求可以公开访问？
- 如何处理认证失败？
- 如何处理授权失败？
- 需要哪些安全措施？

HttpSecurity提供了流式API来配置这些安全规则。

**为什么需要它**：
- ✅ **配置化**：通过代码配置安全规则
- ✅ **链式调用**：流式API，代码可读性好
- ✅ **灵活性**：可以配置各种安全场景
- ✅ **扩展性**：支持自定义配置

### 3.3 配置流程

**典型的HttpSecurity配置流程**：

```java
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            // 1. 基础安全配置
            .csrf().disable()
            
            // 2. Session管理
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            
            // 3. 授权配置
            .authorizeRequests()
                .antMatchers("/public/**").permitAll()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            
            // 4. 添加过滤器
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
            
            // 5. 异常处理
            .exceptionHandling()
                .authenticationEntryPoint(customEntryPoint)
                .accessDeniedHandler(customAccessDeniedHandler);
    }
}
```

### 3.4 API详解

#### 核心方法：csrf()

**方法签名**：
```java
public CsrfConfigurer<HttpSecurity> csrf()
```

**作用**：配置CSRF（跨站请求伪造）保护

**常见配置**：
```java
// 禁用CSRF（JWT应用通常禁用）
http.csrf().disable();

// 自定义CSRF配置
http.csrf()
    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
    .ignoringAntMatchers("/api/public/**");
```

**为什么JWT应用禁用CSRF**：
- JWT Token通常放在请求头中
- CSRF攻击依赖Cookie中的Session
- JWT是无状态的，不使用Session

#### 核心方法：sessionManagement()

**方法签名**：
```java
public SessionManagementConfigurer<HttpSecurity> sessionManagement()
```

**作用**：配置Session管理策略

**重要配置**：
```java
http.sessionManagement()
    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    .maximumSessions(1)  // 最多同时在线1个session
    .expiredUrl("/login?expired");  // session过期跳转
```

**SessionCreationPolicy枚举**：
- `ALWAYS`：总是创建Session
- `NEVER`：不创建Session，但如果存在则使用
- `IF_REQUIRED`：需要时创建（默认）
- `STATELESS`：完全无状态（JWT使用）

#### 核心方法：authorizeRequests()

**方法签名**：
```java
public ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry authorizeRequests()
```

**作用**：配置URL授权规则

**配置模式**：
```java
http.authorizeRequests()
    // 1. 具体路径配置（顺序重要）
    .antMatchers("/api/public/**").permitAll()
    .antMatchers("/api/admin/**").hasRole("ADMIN")
    
    // 2. 基于表达式的配置
    .antMatchers("/api/user/**").hasAnyRole("USER", "ADMIN")
    .antMatchers(HttpMethod.POST, "/api/posts").authenticated()
    
    // 3. 默认规则
    .anyRequest().authenticated();
```

**路径匹配器**：
- `antMatchers()`：Ant风格路径匹配
- `regexMatchers()`：正则表达式匹配
- `requestMatchers()`：基于RequestMatcher

**授权表达式**：
- `permitAll()`：允许所有人
- `denyAll()`：拒绝所有人
- `authenticated()`：需要认证
- `anonymous()`：匿名用户
- `hasRole("ADMIN")`：需要角色
- `hasAuthority("READ")`：需要权限

#### 核心方法：addFilterBefore()

**方法签名**：
```java
public HttpSecurity addFilterBefore(Filter filter, 
                                   Class<? extends Filter> beforeFilter)
```

**作用**：在指定过滤器之前添加自定义过滤器

**参数说明**：

| 参数 | 类型 | 说明 |
|------|------|------|
| `filter` | `Filter` | 要添加的过滤器 |
| `beforeFilter` | `Class<? extends Filter>` | 参照过滤器类 |

**使用示例**：
```java
// 在UsernamePasswordAuthenticationFilter之前添加JWT过滤器
http.addFilterBefore(jwtAuthenticationFilter, 
                    UsernamePasswordAuthenticationFilter.class);
```

**其他添加方法**：
```java
// 在指定过滤器之后添加
http.addFilterAfter(filter, AfterFilter.class);

// 添加到过滤器链末尾
http.addFilter(filter);

// 在指定位置添加
http.addFilterAt(filter, SecurityWebFiltersOrder.FORM_LOGIN_FILTER);
```

#### 核心方法：exceptionHandling()

**方法签名**：
```java
public ExceptionHandlingConfigurer<HttpSecurity> exceptionHandling()
```

**作用**：配置异常处理

**配置示例**：
```java
http.exceptionHandling()
    .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
    .accessDeniedHandler(new AccessDeniedHandlerImpl());
```

### 3.5 完整案例代码

#### 案例1：JWT应用的完整配置

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            // 1. 禁用CSRF（JWT无状态应用）
            .csrf().disable()
            
            // 2. 配置无状态Session（JWT不需要Session）
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            
            // 3. 配置授权规则
            .authorizeRequests()
                // 公开接口
                .antMatchers("/api/login", "/api/register").permitAll()
                .antMatchers("/api/public/**").permitAll()
                .antMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                
                // 受保护接口
                .antMatchers("/api/admin/**").hasRole("ADMIN")
                .antMatchers("/api/user/**").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.GET, "/api/posts/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/posts").authenticated()
                
                // 其他请求都需要认证
                .anyRequest().authenticated()
            
            // 4. 添加JWT过滤器
            .addFilterBefore(jwtAuthenticationFilter, 
                           UsernamePasswordAuthenticationFilter.class)
            
            // 5. 配置异常处理
            .exceptionHandling()
                .authenticationEntryPoint((request, response, authException) -> {
                    response.setContentType("application/json;charset=UTF-8");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write(
                        "{\"code\":401,\"message\":\"未登录或Token已过期\",\"data\":null}"
                    );
                })
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    response.setContentType("application/json;charset=UTF-8");
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.getWriter().write(
                        "{\"code\":403,\"message\":\"权限不足\",\"data\":null}"
                    );
                });
    }
}
```

#### 案例2：传统Session应用的配置

```java
@Configuration
@EnableWebSecurity
public class SessionSecurityConfig extends WebSecurityConfigurerAdapter {
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            // 1. 启用CSRF保护（Session应用需要）
            .csrf()
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            
            // 2. 配置Session管理
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .maximumSessions(1)
                .expiredUrl("/login?expired")
            
            // 3. 配置登录
            .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/home")
                .failureUrl("/login?error")
            
            // 4. 配置登出
            .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
            
            // 5. 配置授权
            .authorizeRequests()
                .antMatchers("/login", "/css/**", "/js/**").permitAll()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated();
    }
}
```

#### 案例3：REST API的配置

```java
@Configuration
@EnableWebSecurity
public class RestSecurityConfig extends WebSecurityConfigurerAdapter {
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            // 1. 禁用CSRF（REST API通常禁用）
            .csrf().disable()
            
            // 2. 允许跨域
            .cors()
            
            // 3. 无状态Session
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            
            // 4. 配置授权
            .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll() // 预检请求
                .antMatchers("/api/auth/**").permitAll()
                .antMatchers(HttpMethod.GET, "/api/public/**").permitAll()
                .antMatchers("/api/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            
            // 5. REST风格的异常处理
            .exceptionHandling()
                .authenticationEntryPoint(new RestAuthenticationEntryPoint())
                .accessDeniedHandler(new RestAccessDeniedHandler());
    }
}
```

### 3.6 配置顺序的重要性

**错误配置示例**：

```java
// ❌ 错误：通配路径在前，具体路径无效
http.authorizeRequests()
    .antMatchers("/api/**").authenticated()     // 匹配所有/api/**路径
    .antMatchers("/api/public/**").permitAll()  // 永远不会执行到这里
```

**正确配置**：

```java
// ✅ 正确：具体路径在前，通配路径在后
http.authorizeRequests()
    .antMatchers("/api/public/**").permitAll()  // 具体路径先匹配
    .antMatchers("/api/**").authenticated()     // 通配路径后匹配
```

### 3.7 注意事项

1. **配置顺序**
   - 基础配置（csrf、session）在前
   - 授权配置在中间
   - 过滤器和异常处理在最后

2. **路径匹配**
   - 使用Ant风格：`/**`、`*`、`?`
   - 具体路径优先于通配路径
   - HTTP方法可以组合使用

3. **性能考虑**
   - 避免过于复杂的路径匹配
   - 合理使用缓存

4. **安全考虑**
   - 不要过度放开权限
   - 定期review安全配置

---

## 4. Authentication API详解

### 4.1 API基本信息

- **接口名**：`org.springframework.security.core.Authentication`
- **包路径**：`org.springframework.security.core`
- **版本要求**：Spring Security 3.0+

### 4.2 核心方法

**主要方法**：
- `getName()`：获取用户名
- `getAuthorities()`：获取权限列表
- `getCredentials()`：获取凭证
- `getDetails()`：获取详细信息
- `isAuthenticated()`：是否已认证
- `setAuthenticated(boolean)`：设置认证状态

### 4.3 使用示例

```java
// 获取当前认证信息
Authentication auth = SecurityContextHolder.getContext().getAuthentication();

// 检查是否已认证
if (auth != null && auth.isAuthenticated()) {
    String username = auth.getName();
    Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
}
```

---

## 5. GrantedAuthority API详解

### 5.1 API基本信息

- **接口名**：`org.springframework.security.core.GrantedAuthority`
- **包路径**：`org.springframework.security.core`
- **版本要求**：Spring Security 3.0+

### 5.2 核心方法

**主要方法**：
- `getAuthority()`：获取权限字符串

### 5.3 常用实现类

- `SimpleGrantedAuthority`：简单权限实现
- `SimpleGrantedAuthority(String role)`：构造函数

### 5.4 使用示例

```java
// 创建权限
GrantedAuthority userRole = new SimpleGrantedAuthority("ROLE_USER");
GrantedAuthority readPerm = new SimpleGrantedAuthority("READ_USER");

// 获取权限字符串
String authority = userRole.getAuthority(); // "ROLE_USER"
```

---

## 📝 总结

### 关键API记忆

1. **SecurityContextHolder** - 全局访问认证信息
   - `getContext()` - 获取SecurityContext
   - `setContext()` - 设置SecurityContext

2. **UsernamePasswordAuthenticationToken** - 认证对象
   - 两个参数构造函数：未认证状态
   - 三个参数构造函数：已认证状态

3. **HttpSecurity** - 安全配置
   - `csrf().disable()` - 禁用CSRF
   - `sessionManagement()` - Session配置
   - `authorizeRequests()` - 授权配置
   - `addFilterBefore()` - 添加过滤器

### 最佳实践

- ✅ JWT应用禁用CSRF和Session
- ✅ 具体路径在前，通配路径在后
- ✅ 使用addFilterBefore()正确添加过滤器
- ✅ 统一的异常处理

---

*文档更新时间：2025年12月2日*

