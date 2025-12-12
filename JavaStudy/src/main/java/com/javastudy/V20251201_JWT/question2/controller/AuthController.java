package com.javastudy.V20251201_JWT.question2.controller;

import com.javastudy.V20251201_JWT.question1.jwtUtil;
import com.javastudy.V20251201_JWT.question2.common.Result;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 认证控制器
 * 
 * 作用：提供测试接口，验证JWT认证功能
 * 
 * 接口说明：
 * 1. POST /api/login - 登录接口（公开，不需要Token）
 * 2. GET /api/user/info - 获取用户信息（受保护，需要Token）
 * 3. GET /api/public/hello - 公开接口（不需要Token）
 * 
 * 学习重点：
 * - 公开接口和受保护接口的区别
 * - 如何获取当前登录用户信息
 * - 如何使用统一响应格式
 * - 如何测试JWT认证功能
 */
@RestController
@RequestMapping("/api")
public class AuthController {

    // TODO: 注入JWT工具类（用于生成Token）
    private jwtUtil jwtUtil;

    public AuthController(jwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * 登录接口（公开接口，不需要Token）
     * 
     * 作用：模拟用户登录，返回JWT Token
     * 
     * 请求示例：
     * POST /api/login
     * Content-Type: application/json
     * {
     *   "username": "admin",
     *   "password": "123456"
     * }
     * 
     * 响应示例：
     * {
     *   "code": 200,
     *   "message": "登录成功",
     *   "data": {
     *     "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
     *   }
     * }
     * 
     * @param loginRequest 登录请求（包含用户名和密码）
     * @return 包含Token的响应
     */
    @PostMapping("/login")
    public Result<Map<String, String>> login(@RequestBody Map<String, String> loginRequest) {
        // TODO: 步骤1 - 获取用户名和密码
        // 提示：
        // 1. 从loginRequest中获取username和password
        // 2. 简单的验证（实际项目中应该查询数据库验证）
        // 
        // 示例：
        // String username = loginRequest.get("username");
        // String password = loginRequest.get("password");
        
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");
        
        // TODO: 步骤2 - 验证用户名和密码（简化版）
        // 提示：
        // 1. 这里简化处理，实际项目中应该查询数据库
        // 2. 如果验证失败，返回错误信息
        // 
        // 思考：
        // - 实际项目中如何验证用户名和密码？
        // - 密码应该存储什么格式？（加密后的哈希值）
        
        if (username == null || password == null) {
            return Result.fail(400, "用户名和密码不能为空");
        }
        
        // 简化验证：任何用户名和密码都可以登录（仅用于测试）
        // 实际项目中应该查询数据库验证
        
        // TODO: 步骤3 - 生成JWT Token
        // 提示：
        // 1. 使用 jwtUtil.generateToken(username) 生成Token
        // 2. 将Token放入响应数据中
        // 
        // 思考：
        // - Token应该包含哪些信息？
        // - Token的有效期是多久？
        
        // String token = jwtUtil.generateToken(username); // TODO: 生成Token
        
        // TODO: 步骤4 - 返回成功响应
        // 提示：
        // 1. 创建响应数据Map，包含token字段
        // 2. 使用 Result.success(data) 返回成功响应
        // 
        // 示例：
        // Map<String, String> data = new HashMap<>();
        // data.put("token", token);
        // return Result.success(data);
        
        // 临时返回，实际应该使用上面的逻辑
        Map<String, String> data = new HashMap<>();
        data.put("token", "temp_token"); // TODO: 替换为实际生成的Token
        return Result.success(data);
    }

    /**
     * 获取当前用户信息（受保护接口，需要Token）
     * 
     * 作用：验证JWT Token是否有效，返回当前登录用户信息
     * 
     * 请求示例：
     * GET /api/user/info
     * Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
     * 
     * 响应示例：
     * {
     *   "code": 200,
     *   "message": "success",
     *   "data": {
     *     "username": "admin"
     *   }
     * }
     * 
     * 学习重点：
     * - 如何获取当前登录用户信息？
     * - SecurityContextHolder的使用
     * - Authentication对象的使用
     * 
     * @return 当前用户信息
     */
    @GetMapping("/user/info")
    public Result<Map<String, String>> getUserInfo() {
        // TODO: 步骤1 - 获取当前认证信息
        // 提示：
        // 1. 使用 SecurityContextHolder.getContext().getAuthentication() 获取Authentication
        // 2. 检查Authentication是否为null
        // 3. 如果为null或未认证，返回401错误
        // 
        // 思考：
        // - 为什么可以通过SecurityContextHolder获取认证信息？
        // - 如果Token无效，Authentication会是null吗？
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        if (auth == null || !auth.isAuthenticated()) {
            return Result.fail(401, "未登录");
        }
        
        // TODO: 步骤2 - 获取用户名
        // 提示：
        // 1. 使用 auth.getName() 获取用户名
        // 2. 用户名是Token中的subject（subject是在生成Token时设置的）
        
        String username = auth.getName();
        
        // TODO: 步骤3 - 构建响应数据
        // 提示：
        // 1. 创建Map包含用户信息
        // 2. 可以包含用户名、权限等信息
        // 
        // 思考：
        // - 还可以返回哪些用户信息？
        // - 如何获取用户的权限信息？
        
        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("username", username);
        // 可以添加更多信息，如权限、角色等
        
        // TODO: 步骤4 - 返回成功响应
        return Result.success(userInfo);
    }

    /**
     * 公开接口（不需要Token）
     * 
     * 作用：测试公开接口是否可以正常访问
     * 
     * 请求示例：
     * GET /api/public/hello
     * 
     * 响应示例：
     * {
     *   "code": 200,
     *   "message": "success",
     *   "data": {
     *     "message": "这是公开接口，不需要Token"
     *   }
     * }
     * 
     * @return 公开消息
     */
    @GetMapping("/public/hello")
    public Result<Map<String, String>> publicHello() {
        // TODO: 实现公开接口
        // 提示：
        // 1. 不需要验证Token
        // 2. 直接返回消息即可
        
        Map<String, String> data = new HashMap<>();
        data.put("message", "这是公开接口，不需要Token");
        return Result.success(data);
    }
}

