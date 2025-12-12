package com.javastudy.V20251201_JWT.question2.config;

import com.javastudy.V20251201_JWT.question1.jwtUtil;
import com.javastudy.V20251201_JWT.question2.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security配置类
 * 
 * 作用：配置Spring Security的安全规则，包括：
 * 1. 哪些路径需要认证，哪些路径公开访问
 * 2. 配置JWT过滤器
 * 3. 配置Session管理（无状态）
 * 4. 配置异常处理
 * 
 * 学习重点：
 * - @EnableWebSecurity：启用Spring Security
 * - WebSecurityConfigurerAdapter：Spring Security配置基类
 * - HttpSecurity：HTTP安全配置对象
 * - 过滤器链配置：如何添加自定义过滤器？
 * - 授权配置：如何配置路径的访问权限？
 * - Session配置：无状态Session的配置
 */
@Configuration
@EnableWebSecurity  // TODO: 添加注解启用Spring Security
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // TODO: 注入JWT工具类（用于创建JWT过滤器）
    // 提示：使用@Autowired注入，或者通过构造函数注入
    private jwtUtil jwtUtil;

    public SecurityConfig(jwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * 配置HTTP安全规则
     * 
     * 配置流程：
     * 1. 禁用CSRF（JWT无状态应用通常禁用）
     * 2. 配置Session管理为无状态
     * 3. 配置路径授权规则
     * 4. 添加JWT过滤器
     * 5. 配置异常处理
     * 
     * @param http HttpSecurity对象，用于配置HTTP安全规则
     * @throws Exception 配置异常
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        
        // TODO: 步骤1 - 禁用CSRF
        // 提示：
        // 1. JWT无状态应用通常禁用CSRF保护
        // 2. CSRF主要用于防止跨站请求伪造，但JWT Token通常放在请求头中，不受CSRF影响
        // 3. 使用 http.csrf().disable()
        // 
        // 思考：
        // - 什么时候需要CSRF？什么时候可以禁用？
        // - 禁用CSRF有什么风险？
        
        // http.csrf().disable(); // TODO: 禁用CSRF
        
        // TODO: 步骤2 - 配置Session管理为无状态
        // 提示：
        // 1. JWT是无状态认证，不需要Session
        // 2. 使用 http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        // 3. SessionCreationPolicy.STATELESS：完全无状态，不创建也不使用Session
        // 
        // 思考：
        // - 为什么JWT不需要Session？
        // - 无状态Session有什么优势？
        
        // http.sessionManagement()
        //     .sessionCreationPolicy(SessionCreationPolicy.STATELESS); // TODO: 配置无状态Session
        
        // TODO: 步骤3 - 配置路径授权规则
        // 提示：
        // 1. 使用 http.authorizeRequests() 开始配置授权规则
        // 2. 公开路径：使用 .antMatchers().permitAll()
        //    - /api/login：登录接口
        //    - /api/public/**：公开接口
        //    - /swagger-ui/**：Swagger文档（如果有）
        // 3. 受保护路径：使用 .authenticated() 表示需要认证
        // 4. 最后使用 .anyRequest().authenticated() 表示其他请求都需要认证
        // 
        // 参考API：
        // - authorizeRequests()：开始配置授权规则
        // - antMatchers(String... patterns)：匹配路径模式
        // - permitAll()：允许所有人访问
        // - authenticated()：需要认证
        // - anyRequest()：匹配所有请求
        // 
        // 思考：
        // - 哪些路径应该公开访问？
        // - 配置顺序重要吗？是的，按顺序匹配，匹配到就停止
        // - antMatchers中的路径模式是什么？支持Ant路径模式
        
        // http.authorizeRequests()
        //     .antMatchers("/api/login", "/api/public/**").permitAll()  // TODO: 公开路径
        //     .antMatchers("/api/**").authenticated()                    // TODO: 受保护路径
        //     .anyRequest().authenticated();                             // TODO: 其他请求需要认证
        
        // TODO: 步骤4 - 添加JWT过滤器
        // 提示：
        // 1. 创建JwtAuthenticationFilter实例（需要传入jwtUtil）
        // 2. 使用 http.addFilterBefore() 添加过滤器
        // 3. 放在 UsernamePasswordAuthenticationFilter 之前
        // 
        // 参考API：
        // - addFilterBefore(Filter filter, Class<? extends Filter> beforeFilter)
        // - new JwtAuthenticationFilter(jwtUtil)
        // 
        // 思考：
        // - 为什么要放在UsernamePasswordAuthenticationFilter之前？
        // - 过滤器顺序重要吗？非常重要！
        
        // JwtAuthenticationFilter jwtFilter = new JwtAuthenticationFilter(jwtUtil);
        // http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class); // TODO: 添加JWT过滤器
        
        // TODO: 步骤5 - 配置异常处理（可选，进阶功能）
        // 提示：
        // 1. 认证失败处理：使用 .authenticationEntryPoint()
        // 2. 授权失败处理：使用 .accessDeniedHandler()
        // 3. 自定义返回格式，返回统一的JSON响应
        // 
        // 思考：
        // - 什么时候会触发认证失败？什么时候会触发授权失败？
        // - 默认的异常处理是什么？返回什么响应？
        
        // http.exceptionHandling()
        //     .authenticationEntryPoint(...)  // TODO: 认证失败处理
        //     .accessDeniedHandler(...);      // TODO: 授权失败处理
    }
    
    /**
     * 创建JWT过滤器Bean
     * 
     * 作用：创建JwtAuthenticationFilter实例，供Spring Security使用
     * 
     * 为什么用@Bean？
     * - 让Spring管理过滤器生命周期
     * - 便于依赖注入
     * - 便于测试
     * 
     * @return JwtAuthenticationFilter实例
     */
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        // TODO: 创建并返回JwtAuthenticationFilter实例
        // 提示：需要传入jwtUtil参数
        // return new JwtAuthenticationFilter(jwtUtil);
        
        return null; // TODO: 返回过滤器实例
    }
}

