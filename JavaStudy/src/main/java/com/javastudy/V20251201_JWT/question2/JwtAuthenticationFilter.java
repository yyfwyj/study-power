package com.javastudy.V20251201_JWT.question2;

import com.javastudy.V20251201_JWT.question1.jwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * JWT认证过滤器
 * 
 * 作用：从请求头中提取JWT Token，验证Token，并将认证信息设置到SecurityContext中
 * 
 * 设计思路：
 * 1. 继承OncePerRequestFilter，确保每个请求只执行一次
 * 2. 从Authorization请求头提取Bearer Token
 * 3. 验证Token的有效性
 * 4. 如果Token有效，创建Authentication对象并设置到SecurityContext
 * 5. 无论验证成功还是失败，都继续执行过滤器链（让Spring Security统一处理）
 * 
 * 学习重点：
 * - OncePerRequestFilter的使用：为什么用它？解决了什么问题？
 * - HttpServletRequest.getHeader()：如何提取请求头？
 * - FilterChain.doFilter()：为什么要调用？什么时候调用？
 * - SecurityContextHolder：如何设置和获取认证信息？
 * - 优雅失败：验证失败时如何处理？
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // TODO: 注入JWT工具类
    // 提示：使用@Autowired注入，或者通过构造函数注入
    // 提示：先创建一个JwtUtil实例变量
    private jwtUtil jwtUtil;

    /**
     * 构造函数注入JwtUtil
     * 
     * 为什么用构造函数注入？
     * - 强制依赖：确保JwtUtil不为null
     * - 便于测试：可以传入Mock对象
     * - 不可变性：final字段确保不可变
     */
    public JwtAuthenticationFilter(jwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * 判断当前请求是否应该跳过过滤器
     * 
     * 使用场景：
     * - 跳过公开路径（登录、注册等）
     * - 跳过静态资源
     * - 跳过OPTIONS请求（CORS预检）
     * 
     * 思考：
     * - 哪些路径不需要JWT验证？
     * - 如何判断请求路径？
     * - 使用HttpServletRequest的哪个方法获取路径？
     * 
     * @param request HTTP请求对象
     * @return true表示跳过过滤器，false表示执行过滤器
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        // TODO: 实现路径判断逻辑
        // 提示：
        // 1. 获取请求URI：request.getRequestURI()
        // 2. 获取请求方法：request.getMethod()
        // 3. 跳过OPTIONS请求（CORS预检）
        // 4. 跳过公开路径（如 /api/login, /api/public 等）
        // 
        // 示例代码框架：
        // String uri = request.getRequestURI();
        // String method = request.getMethod();
        // if ("OPTIONS".equals(method)) { return true; }
        // if (uri.startsWith("/api/public")) { return true; }
        // return false;
        
        return false; // 默认不跳过
    }

    /**
     * 核心方法：执行过滤逻辑
     * 
     * 执行流程：
     * 1. 从请求头提取Token
     * 2. 验证Token的有效性
     * 3. 如果Token有效，创建Authentication对象
     * 4. 设置到SecurityContext
     * 5. 继续执行过滤器链
     * 
     * 关键点：
     * - 无论验证成功还是失败，都必须调用filterChain.doFilter()
     * - 验证失败时，不设置认证信息即可，让Spring Security返回401
     * - 不要抛出异常，记录日志即可
     * 
     * @param request HTTP请求对象
     * @param response HTTP响应对象
     * @param filterChain 过滤器链
     * @throws ServletException Servlet异常
     * @throws IOException IO异常
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        
        // TODO: 步骤1 - 从请求头提取Token
        // 提示：
        // 1. 使用 request.getHeader("Authorization") 获取Authorization头
        // 2. 检查是否为null
        // 3. 检查是否有"Bearer "前缀（注意：Bearer后面有空格）
        // 4. 提取Token字符串（去掉"Bearer "前缀，共7个字符）
        // 5. 检查Token是否为空
        // 
        // 参考API：HttpServletRequest.getHeader(String name)
        // 参考API：String.startsWith(String prefix)
        // 参考API：String.substring(int beginIndex)
        // 
        // 思考：
        // - Authorization头的格式是什么？ "Bearer <token>"
        // - 如果没有Authorization头怎么办？返回null，继续执行过滤器链
        // - Token提取失败怎么办？记录日志，继续执行过滤器链
        
        String token = null; // TODO: 实现Token提取逻辑
        
        // TODO: 步骤2 - 验证Token
        // 提示：
        // 1. 如果token为null，跳过验证（直接执行步骤5）
        // 2. 使用 jwtUtil.getUsernameFromToken(token) 验证Token
        // 3. 检查返回值：
        //    - 如果返回的是错误信息（以"Token"开头），说明Token无效
        //    - 如果返回的是用户名，说明Token有效
        // 
        // 思考：
        // - Token验证失败时如何处理？
        // - 验证失败时应该设置认证信息吗？
        // - 如何判断Token是否有效？
        
        if (token != null) {
            // TODO: 调用jwtUtil验证Token
            String username = null; // TODO: jwtUtil.getUsernameFromToken(token);
            
            // TODO: 步骤3 - 检查Token是否有效
            // 提示：
            // 1. 检查username是否为null
            // 2. 检查username是否以"Token"开头（这是jwtUtil返回错误信息的格式）
            // 3. 如果Token有效，继续步骤4
            // 4. 如果Token无效，记录日志，继续执行过滤器链（不设置认证信息）
            
            if (username != null && !username.startsWith("Token")) {
                // TODO: 步骤4 - 创建Authentication对象并设置到SecurityContext
                // 提示：
                // 1. 创建权限列表（GrantedAuthority）
                //    - 可以先创建一个简单的权限，如 new SimpleGrantedAuthority("ROLE_USER")
                //    - 后续可以从Token中提取权限信息
                // 2. 创建UsernamePasswordAuthenticationToken对象
                //    参数：username（主体），null（凭证，JWT中不需要），authorities（权限列表）
                // 3. 设置为已认证：auth.setAuthenticated(true)
                // 4. 设置到SecurityContext：
                //    SecurityContextHolder.getContext().setAuthentication(auth)
                // 
                // 参考API：
                // - new SimpleGrantedAuthority(String role)
                // - new UsernamePasswordAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities)
                // - Authentication.setAuthenticated(boolean isAuthenticated)
                // - SecurityContextHolder.getContext().setAuthentication(Authentication auth)
                // 
                // 思考：
                // - 为什么credentials传null？JWT中不需要密码
                // - 权限信息从哪里来？可以从Token的claims中提取
                // - SecurityContext是线程安全的吗？是的，使用ThreadLocal
                
                // 创建权限列表（示例：默认ROLE_USER，实际应该从Token中提取）
                List<GrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
                
                // TODO: 创建Authentication对象
                // Authentication auth = new UsernamePasswordAuthenticationToken(...);
                
                // TODO: 设置为已认证
                // auth.setAuthenticated(true);
                
                // TODO: 设置到SecurityContext
                // SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        
        // TODO: 步骤5 - 继续执行过滤器链
        // 提示：
        // 1. 无论验证成功还是失败，都必须调用filterChain.doFilter()
        // 2. 这是最关键的一步！不调用会导致请求被拦截
        // 3. 传递request和response对象
        // 
        // 思考：
        // - 为什么要调用filterChain.doFilter()？
        // - 如果不调用会怎样？
        // - 调用时机是什么时候？
        
        // filterChain.doFilter(request, response);
    }
    
    /**
     * 辅助方法：从请求头提取Token
     * 
     * 作用：封装Token提取逻辑，提高代码可读性
     * 
     * 实现步骤：
     * 1. 获取Authorization请求头
     * 2. 检查是否为null或空
     * 3. 检查是否有"Bearer "前缀（不区分大小写）
     * 4. 提取Token字符串
     * 5. 返回Token（如果提取失败返回null）
     * 
     * @param request HTTP请求对象
     * @return Token字符串，如果提取失败返回null
     */
    private String extractToken(HttpServletRequest request) {
        // TODO: 实现Token提取逻辑
        // 提示：参考上面的步骤1注释
        // 
        // 企业级实现应该包含：
        // - 完整的null检查
        // - 日志记录
        // - 异常处理
        
        return null; // TODO: 返回提取的Token
    }
    
    /**
     * 辅助方法：从Token中提取权限信息
     * 
     * 作用：从Token的claims中提取用户的角色和权限
     * 
     * 实现思路：
     * 1. 解析Token获取Claims
     * 2. 从Claims中提取权限信息（可能需要自定义claim）
     * 3. 转换为GrantedAuthority列表
     * 
     * 注意：这是进阶功能，初始实现可以返回默认权限
     * 
     * @param token JWT Token
     * @return 权限列表
     */
    private List<GrantedAuthority> getAuthoritiesFromToken(String token) {
        // TODO: 实现权限提取逻辑（进阶功能）
        // 提示：
        // 1. 可以在生成Token时，将权限信息放入claims
        // 2. 在这里解析Token，提取权限信息
        // 3. 转换为SimpleGrantedAuthority对象
        
        // 初始实现：返回默认权限
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        return authorities;
    }
}
