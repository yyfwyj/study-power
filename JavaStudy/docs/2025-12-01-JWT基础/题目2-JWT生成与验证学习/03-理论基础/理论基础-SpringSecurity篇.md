# 理论基础：Spring Security核心概念

## 📖 目录

- [1. Spring Security概述](#1-spring-security概述)
- [2. 核心概念详解](#2-核心概念详解)
- [3. 认证与授权](#3-认证与授权)
- [4. SecurityContext详解](#4-securitycontext详解)
- [5. 配置原理](#5-配置原理)

---

## 1. Spring Security概述

### 1.1 什么是Spring Security

**Spring Security** 是Spring生态系统中的安全框架，提供了：

- **认证（Authentication）**：你是谁？验证用户身份
- **授权（Authorization）**：你能做什么？检查用户权限
- **攻击防护**：CSRF、XSS、SQL注入等
- **Session管理**：会话创建、超时、并发控制

### 1.2 为什么需要Spring Security

**传统方式的痛点**：

```java
// ❌ 没有Spring Security时，需要在每个Controller中检查
@GetMapping("/user/info")
public String getUserInfo(HttpServletRequest request) {
    String token = request.getHeader("Authorization");
    if (token == null) {
        return "未登录"; // 重复的认证逻辑
    }
    // 验证Token...
    // 检查权限...
    // 业务逻辑...
}
```

**Spring Security的优势**：

- ✅ **声明式安全**：通过配置声明安全规则，而不是写代码
- ✅ **自动化处理**：自动拦截请求，自动处理认证授权
- ✅ **统一管理**：所有安全逻辑集中配置
- ✅ **标准化**：遵循业界标准（OAuth2、JWT等）

### 1.3 Spring Security的核心思想

**"约定优于配置"**：
- 默认配置已经足够安全
- 可以通过配置覆盖默认行为
- 减少样板代码

**"过滤器链"**：
- 所有请求都经过过滤器链
- 每个过滤器负责特定的安全功能
- 可以自定义过滤器

---

## 2. 核心概念详解

### 2.1 Authentication（认证对象）

**Authentication** 代表当前请求的认证信息。

**核心字段**：

```java
public interface Authentication extends Principal, Serializable {
    Collection<? extends GrantedAuthority> getAuthorities();  // 权限集合
    Object getCredentials();                                   // 凭证（如密码，认证后通常为null）
    Object getDetails();                                       // 详细信息
    Object getPrincipal();                                     // 主体（通常是UserDetails对象或用户名）
    boolean isAuthenticated();                                 // 是否已认证
    void setAuthenticated(boolean isAuthenticated);           // 设置认证状态
}
```

**Authentication的状态**：

1. **未认证状态**
   - `isAuthenticated() = false`
   - `principal` 可能是用户名（String）
   - `credentials` 可能是密码

2. **已认证状态**
   - `isAuthenticated() = true`
   - `principal` 是UserDetails对象或用户名
   - `credentials` 通常为null（安全考虑，认证后清除密码）

**常见实现类**：

- `UsernamePasswordAuthenticationToken` - 用户名密码认证
- `PreAuthenticatedAuthenticationToken` - 预认证Token
- `AnonymousAuthenticationToken` - 匿名用户

### 2.2 SecurityContext（安全上下文）

**SecurityContext** 存储当前请求的安全信息，主要是Authentication对象。

```java
public interface SecurityContext extends Serializable {
    Authentication getAuthentication();
    void setAuthentication(Authentication authentication);
}
```

**关键特性**：

1. **线程局部变量（ThreadLocal）**
   - 每个请求线程有独立的SecurityContext
   - 请求结束时自动清理
   - 线程安全

2. **存储位置**
   - 默认存储在SecurityContextHolder中（ThreadLocal）
   - 可以配置存储在Session中（用于有状态应用）
   - JWT无状态应用通常不使用Session

### 2.3 SecurityContextHolder（安全上下文持有者）

**SecurityContextHolder** 是SecurityContext的持有者，提供全局访问点。

**获取SecurityContext**：

```java
// 获取当前请求的SecurityContext
SecurityContext context = SecurityContextHolder.getContext();

// 获取Authentication
Authentication auth = SecurityContextHolder.getContext().getAuthentication();

// 获取用户名
String username = SecurityContextHolder.getContext()
    .getAuthentication()
    .getName();
```

**存储策略（MODE）**：

- `MODE_THREADLOCAL`（默认）：使用ThreadLocal存储，每个线程独立
- `MODE_INHERITABLETHREADLOCAL`：子线程可以继承父线程的SecurityContext
- `MODE_GLOBAL`：全局共享一个SecurityContext（很少使用，不安全）

### 2.4 GrantedAuthority（权限）

**GrantedAuthority** 代表授予用户的权限或角色。

```java
public interface GrantedAuthority extends Serializable {
    String getAuthority();  // 返回权限字符串，如 "ROLE_USER" 或 "READ_USER"
}
```

**常见实现**：

```java
// 角色权限
SimpleGrantedAuthority role = new SimpleGrantedAuthority("ROLE_USER");

// 功能权限
SimpleGrantedAuthority permission = new SimpleGrantedAuthority("READ_USER");
```

**角色 vs 权限**：

- **角色（Role）**：通常以 `ROLE_` 开头，如 `ROLE_USER`、`ROLE_ADMIN`
- **权限（Permission）**：具体的操作权限，如 `READ_USER`、`WRITE_USER`

**Spring Security默认行为**：
- 检查角色时，会自动添加 `ROLE_` 前缀
- 例如：`hasRole("USER")` 实际检查 `ROLE_USER`

### 2.5 UserDetails（用户详情）

**UserDetails** 代表用户信息，包含：

```java
public interface UserDetails extends Serializable {
    Collection<? extends GrantedAuthority> getAuthorities();  // 权限集合
    String getPassword();                                      // 密码
    String getUsername();                                      // 用户名
    boolean isAccountNonExpired();                             // 账户未过期
    boolean isAccountNonLocked();                              // 账户未锁定
    boolean isCredentialsNonExpired();                         // 凭证未过期
    boolean isEnabled();                                       // 账户启用
}
```

**常见实现**：

```java
// Spring Security提供的标准实现
UserDetails user = User.builder()
    .username("admin")
    .password("password")
    .roles("USER", "ADMIN")
    .accountExpired(false)
    .accountLocked(false)
    .credentialsExpired(false)
    .disabled(false)
    .build();
```

**注意**：在JWT认证中，通常不需要完整的UserDetails，只需要用户名和权限信息。

---

## 3. 认证与授权

### 3.1 认证（Authentication）

**认证就是回答"你是谁"的问题**。

**认证流程**：

```
1. 用户提供凭证（用户名密码、Token等）
   ↓
2. AuthenticationManager验证凭证
   ↓
3. 如果有效，创建Authentication对象
   ↓
4. 设置到SecurityContext
   ↓
5. 认证完成
```

**在JWT场景中**：

```java
// 1. 从Token中提取用户名和权限
String username = jwtUtil.getUsernameFromToken(token);
List<GrantedAuthority> authorities = getAuthoritiesFromToken(token);

// 2. 创建Authentication对象（不需要密码验证，因为Token本身就是凭证）
Authentication auth = new UsernamePasswordAuthenticationToken(
    username,           // principal
    null,               // credentials（JWT中不需要）
    authorities         // authorities
);

// 3. 设置为已认证
auth.setAuthenticated(true);

// 4. 设置到SecurityContext
SecurityContextHolder.getContext().setAuthentication(auth);
```

### 3.2 授权（Authorization）

**授权就是回答"你能做什么"的问题**。

**授权检查时机**：

- 在所有过滤器执行完毕后
- 在FilterSecurityInterceptor中执行
- 根据配置的安全规则进行判断

**授权配置示例**：

```java
http.authorizeRequests()
    .antMatchers("/public/**").permitAll()           // 公开访问
    .antMatchers("/admin/**").hasRole("ADMIN")        // 需要ADMIN角色
    .antMatchers("/user/**").hasAnyRole("USER", "ADMIN")  // 需要USER或ADMIN角色
    .anyRequest().authenticated();                    // 其他请求需要认证
```

**授权表达式**：

- `permitAll()` - 允许所有人访问
- `denyAll()` - 拒绝所有人访问
- `authenticated()` - 需要认证
- `hasRole("ADMIN")` - 需要有ADMIN角色
- `hasAnyRole("USER", "ADMIN")` - 需要有任一角色
- `hasAuthority("READ_USER")` - 需要有指定权限
- `hasIpAddress("192.168.1.0/24")` - 需要来自指定IP段

### 3.3 认证 vs 授权

**区别**：

| 方面 | 认证（Authentication） | 授权（Authorization） |
|------|----------------------|---------------------|
| **问题** | 你是谁？ | 你能做什么？ |
| **时机** | 在授权之前 | 在认证之后 |
| **结果** | 创建Authentication对象 | 允许或拒绝访问 |
| **实现** | AuthenticationManager | AccessDecisionManager |

**关系**：

```
未认证 → 认证失败 → 返回401 Unauthorized
    ↓
认证成功
    ↓
已认证 → 授权检查 → 授权失败 → 返回403 Forbidden
    ↓
授权成功
    ↓
允许访问资源
```

---

## 4. SecurityContext详解

### 4.1 SecurityContext的生命周期

**在JWT无状态场景中的生命周期**：

```
1. 请求到达
   SecurityContext为空（新请求，没有Session）
   ↓
2. JwtAuthenticationFilter执行
   提取Token → 验证Token → 创建Authentication → 设置到SecurityContext
   ↓
3. SecurityContext包含认证信息
   后续过滤器可以使用
   ↓
4. Controller执行
   可以通过SecurityContextHolder获取认证信息
   ↓
5. 请求结束
   SecurityContext被清理（ThreadLocal自动清理）
```

**关键点**：
- 每个请求开始，SecurityContext都是空的（无状态）
- JWT过滤器负责填充SecurityContext
- 请求结束后自动清理

### 4.2 如何设置SecurityContext

**在JWT过滤器中的设置**：

```java
// 1. 获取SecurityContext（如果不存在会自动创建）
SecurityContext context = SecurityContextHolder.getContext();

// 2. 创建Authentication对象
Authentication auth = new UsernamePasswordAuthenticationToken(
    username,
    null,
    authorities
);

// 3. 设置为已认证
auth.setAuthenticated(true);

// 4. 设置到SecurityContext
context.setAuthentication(auth);
```

**完整示例**：

```java
// 验证Token
String username = jwtUtil.getUsernameFromToken(token);
if (username != null && !username.startsWith("Token")) {
    // Token有效，创建认证对象
    List<GrantedAuthority> authorities = Arrays.asList(
        new SimpleGrantedAuthority("ROLE_USER")
    );
    
    Authentication auth = new UsernamePasswordAuthenticationToken(
        username,      // principal
        null,          // credentials
        authorities    // authorities
    );
    
    // 设置到SecurityContext
    SecurityContextHolder.getContext().setAuthentication(auth);
}
```

### 4.3 如何获取SecurityContext

**在Controller中获取**：

```java
@RestController
public class UserController {
    
    @GetMapping("/user/info")
    public String getUserInfo() {
        // 方式1：通过SecurityContextHolder获取
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        
        // 方式2：直接注入Principal（更简洁）
        // public String getUserInfo(Principal principal) {
        //     String username = principal.getName();
        // }
        
        return "Hello " + username;
    }
}
```

**在Service中获取**：

```java
@Service
public class UserService {
    
    public String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            return auth.getName();
        }
        return null;
    }
}
```

### 4.4 SecurityContext的线程安全性

**ThreadLocal机制**：

```java
// SecurityContextHolder内部使用ThreadLocal存储
private static final ThreadLocal<SecurityContext> contextHolder = new ThreadLocal<>();

// 每个线程获取的是自己的SecurityContext
SecurityContext context = contextHolder.get();  // 返回当前线程的SecurityContext
```

**这意味着**：
- ✅ 每个请求线程有独立的SecurityContext
- ✅ 线程A设置的认证信息，线程B无法访问
- ✅ 线程安全，无需同步

**注意事项**：
- 异步任务中需要手动传递SecurityContext
- 可以使用 `@Async` 和 `SecurityContextHolder.setContext()` 传递

---

## 5. 配置原理

### 5.1 SecurityConfig的作用

**SecurityConfig** 是Spring Security的核心配置类，用于：

- 配置哪些请求需要认证
- 配置哪些请求公开访问
- 配置过滤器链
- 配置异常处理
- 配置Session管理

### 5.2 配置类的创建

```java
@Configuration
@EnableWebSecurity  // 启用Spring Security
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 配置HTTP安全规则
    }
}
```

**关键注解**：
- `@Configuration` - 标识为配置类
- `@EnableWebSecurity` - 启用Spring Security的Web安全功能

### 5.3 HttpSecurity配置链

**配置链式调用**：

```java
http
    .csrf().disable()                          // 禁用CSRF（JWT无状态应用通常禁用）
    .sessionManagement()                       // Session管理配置
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)  // 无状态
    .authorizeRequests()                       // 授权配置
        .antMatchers("/public/**").permitAll() // 公开路径
        .antMatchers("/api/**").authenticated() // 需要认证
    .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)  // 添加JWT过滤器
    .exceptionHandling()                       // 异常处理配置
        .authenticationEntryPoint(...)         // 认证失败处理
        .accessDeniedHandler(...);             // 授权失败处理
```

**配置顺序很重要**：
- 先配置基础设置（csrf、session）
- 再配置授权规则
- 最后添加过滤器和异常处理

### 5.4 过滤器配置

**添加JWT过滤器**：

```java
http.addFilterBefore(
    jwtAuthenticationFilter,                    // 要添加的过滤器
    UsernamePasswordAuthenticationFilter.class  // 放在这个过滤器之前
);
```

**其他添加过滤器的方法**：
- `addFilterBefore()` - 在指定过滤器之前添加
- `addFilterAfter()` - 在指定过滤器之后添加
- `addFilter()` - 添加到过滤器链末尾
- `addFilterAt()` - 添加到指定位置

**为什么放在UsernamePasswordAuthenticationFilter之前**：
- JWT认证应该在表单登录之前尝试
- 如果JWT认证成功，就不需要表单登录了

### 5.5 Session管理配置

**无状态Session配置**：

```java
http.sessionManagement()
    .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
```

**Session策略**：
- `ALWAYS` - 总是创建Session（有状态）
- `NEVER` - 从不创建Session，但如果已存在则使用
- `IF_REQUIRED` - 如果需要则创建（默认）
- `STATELESS` - 完全无状态，不创建也不使用Session（JWT使用）

---

## 📝 总结

### 关键概念记忆

1. **Authentication** - 认证对象，包含用户信息和权限
2. **SecurityContext** - 安全上下文，存储Authentication
3. **SecurityContextHolder** - SecurityContext的持有者，提供访问入口
4. **认证** - 验证用户身份（你是谁）
5. **授权** - 检查用户权限（你能做什么）

### 核心流程

```
请求到达
  ↓
JWT过滤器提取Token
  ↓
验证Token，创建Authentication
  ↓
设置到SecurityContext
  ↓
授权检查
  ↓
允许/拒绝访问
```

### 下一步学习

理解核心概念后，接下来应该学习：

- [API详解-SpringSecurity篇](../02-API详解/API详解-SpringSecurity篇.md) - 掌握具体的API使用方法
- [理论基础-响应格式篇](./理论基础-响应格式篇.md) - 理解统一响应格式设计

---

*文档更新时间：2025年12月2日*
