package com.javastudy.V20251201_JWT.question2;

import com.javastudy.V20251201_JWT.question1.jwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JwtAuthenticationFilter2 extends OncePerRequestFilter {

    // 注入JWT工具类
    private final jwtUtil jwtUtil;

    private final List<String> apiList = Arrays.asList(
            "/api/login",
            "/api/public",
            "/api/static/*",
            "/api/css/*",
            "/api/js/*",
            "/api/images/*",
            "/api/fonts/*",
            "/api/favicon.ico"
    );

    public JwtAuthenticationFilter2(jwtUtil jwtUtil){
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // TODO: 实现路径判断逻辑
        // 提示：
        // 1. 获取请求URI：request.getRequestURI()
        // 2. 获取请求方法：request.getMethod()
        // 3. 跳过OPTIONS请求（CORS预检）
        // 4. 跳过公开路径（如 /api/login, /api/public 等）
        // 5. 跳过静态资源（如 .css, .js, .png, .jpg 等）
        return request.getMethod().equals("OPTIONS") ||
                apiList.stream()
                        .anyMatch(uri -> request.getRequestURI().startsWith(uri));
    }

//    @Override
//    protected void doFilterInternal() {
//
//    }
}
