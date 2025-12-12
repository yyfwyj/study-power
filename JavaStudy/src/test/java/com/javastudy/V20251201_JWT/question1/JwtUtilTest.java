package com.javastudy.V20251201_JWT.question1;

import org.junit.jupiter.api.Test;

import com.javastudy.V20251201_JWT.question1.jwtUtil;
import com.javastudy.V20251201_JWT.question1.V20251202_JWTutil;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JWT工具类单元测试
 * 用于验证JwtUtil类和V20251202_JWTutil类的功能是否正常
 */
public class JwtUtilTest {

    @Test
    public void testGenerateToken() {
        // 测试token生成
        String username = "testuser";
        String token = new jwtUtil().generateToken(username);

        // 验证token不为空
        assertNotNull(token, "生成的token不应为空");

        // 验证token包含三部分（header.payload.signature）
        String[] parts = token.split("\\.");
        assertEquals(3, parts.length, "JWT token应该包含三部分");

        System.out.println("生成的token: " + token);
    }

    @Test
    public void testGetUsernameFromValidToken() {
        // 测试从有效token中提取用户名
        String username = "alice";
        jwtUtil jwt = new jwtUtil();
        String token = jwt.generateToken(username);

        String extractedUsername = jwt.getUsernameFromToken(token);
        assertEquals(username, extractedUsername, "从token中提取的用户名应该正确");
    }

    @Test
    public void testGetUsernameFromInvalidToken() {
        // 测试无效token的情况
        jwtUtil jwt = new jwtUtil();

        // 测试null token
        String result = jwt.getUsernameFromToken(null);
        System.out.println("null token结果: " + result);

        // 测试空字符串token
        result = jwt.getUsernameFromToken("");
        System.out.println("空字符串token结果: " + result);

        // 测试格式错误的token
        result = jwt.getUsernameFromToken("invalid.token.format");
        System.out.println("格式错误token结果: " + result);

        // 测试篡改的token（这里用一个假的token）
        result = jwt.getUsernameFromToken("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0In0.invalid");
        System.out.println("篡改token结果: " + result);
    }

    @Test
    public void testTokenStructure() {
        // 测试token的基本结构
        jwtUtil jwt = new jwtUtil();
        String token = jwt.generateToken("test");

        // 验证token格式
        assertTrue(token.contains("."), "Token应该包含点分隔符");

        // 验证可以被解析（不抛出异常）
        String username = jwt.getUsernameFromToken(token);
        assertNotNull(username, "应该能够成功解析token");
    }

    @Test
    public void testV20251202_JWTutilGenerateToken() {
        // 测试V20251202_JWTutil的token生成
        V20251202_JWTutil jwt = new V20251202_JWTutil();
        String username = "testuser";
        String token = jwt.generateToken(username);

        // 验证token不为空
        assertNotNull(token, "生成的token不应为空");

        // 验证token包含三部分（header.payload.signature）
        String[] parts = token.split("\\.");
        assertEquals(3, parts.length, "JWT token应该包含三部分");

        System.out.println("V20251202_JWTutil生成的token: " + token);
    }

    @Test
    public void testV20251202_JWTutilGetUsernameFromValidToken() {
        // 测试从有效token中提取用户名
        V20251202_JWTutil jwt = new V20251202_JWTutil();
        String username = "alice";
        String token = jwt.generateToken(username);

        String extractedUsername = jwt.getUsernameFromToken(token);
        assertEquals(username, extractedUsername, "从token中提取的用户名应该正确");
    }

    @Test
    public void testV20251202_JWTutilGetUsernameFromInvalidToken() {
        // 测试无效token的情况
        V20251202_JWTutil jwt = new V20251202_JWTutil();

        // 测试null token
        String result = jwt.getUsernameFromToken(null);
        assertEquals("Token 参数错误", result, "null token应该返回参数错误");

        // 测试空字符串token
        result = jwt.getUsernameFromToken("");
        assertEquals("Token 参数错误", result, "空字符串token应该返回参数错误");

        // 测试格式错误的token
        result = jwt.getUsernameFromToken("invalid.token.format");
        assertEquals("Token 格式错误", result, "格式错误token应该返回格式错误");

        // 测试篡改的token（这里用一个假的token）
        result = jwt.getUsernameFromToken("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0In0.invalid");
        assertEquals("Token 签名验证失败", result, "篡改token应该返回签名验证失败");
    }
}
