# 🧩 题目 2：集成 Spring Security —— 自定义 JWT 认证过滤器（进阶）

## 🎯 目标
理解 JWT 如何在请求链路中自动验证用户身份。

## 📋 背景
你已有一个 Spring Boot Web 项目，现在要实现无状态登录。

## 🔧 要求

创建一个 `JwtAuthenticationFilter`，继承 `OncePerRequestFilter`

### 在 `doFilterInternal` 方法中：
1. **从请求头提取 Token**
   - 请求头：`Authorization`
   - 格式：`Bearer <token>`

2. **验证 Token**
   - 如果 Token 存在且有效（调用题目1的 `JwtUtil` 验证）
   - 从 Token 中获取用户名
   - 构造 `UsernamePasswordAuthenticationToken`
   - 设置到 `SecurityContextHolder`

3. **处理失败情况**
   - 验证失败时，继续执行过滤器链（不中断，让后续处理 401）

### 在 `SecurityConfig` 中注册过滤器
- 放在 `UsernamePasswordAuthenticationFilter` 之前

## 💡 提示
- 使用 `HttpServletRequest.getHeader("Authorization")`
- 注意处理 `null` 和格式错误（如没有 "Bearer " 前缀）
- 不要抛异常，验证失败就跳过

## 🏗️ 关键代码骨架

```java
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            // TODO: 验证 token 并设置认证信息
        }
        chain.doFilter(request, response);
    }
}
```

## 📝 实现步骤

1. **创建 JwtAuthenticationFilter 类**
   - 在 `src/main/java/com/javaseudy/jwt/question2/` 目录下
   - 继承 `OncePerRequestFilter`

2. **实现 Token 提取逻辑**
   - 检查 Authorization 头
   - 验证 Bearer 前缀
   - 提取 token 字符串

3. **实现认证逻辑**
   - 调用 JwtUtil 验证 token
   - 从 token 提取用户名
   - 创建认证对象并设置到 SecurityContext

4. **配置 Spring Security**
   - 创建 SecurityConfig 类
   - 注册 JWT 过滤器

## 🔧 依赖配置
确保 `pom.xml` 中包含：
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

## 🧪 测试验证

### 1. 创建测试控制器
```java
@RestController
public class TestController {

    @GetMapping("/public")
    public String publicEndpoint() {
        return "This is public";
    }

    @GetMapping("/protected")
    public String protectedEndpoint() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return "Hello " + auth.getName() + "!";
    }
}
```

### 2. 测试场景
- **无 token 访问**: `/protected` 应返回 401
- **有效 token 访问**: `/protected` 应返回用户信息
- **无效 token 访问**: `/protected` 应返回 401

## 🏆 验收标准
- ✅ 过滤器正确提取 Bearer token
- ✅ 有效 token 自动设置认证上下文
- ✅ 无效 token 不影响其他请求
- ✅ Spring Security 正确集成

## 📚 学习要点
- Spring Security 过滤器链的工作原理
- OncePerRequestFilter 的作用
- SecurityContextHolder 的使用
- JWT 在 Web 请求中的集成方式

---

*学习日期：2025年12月1日*
*题目难度：⭐⭐⭐⭐☆*
*预计完成时间：45分钟*
