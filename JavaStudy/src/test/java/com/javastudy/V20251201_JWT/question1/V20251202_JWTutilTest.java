package com.javastudy.V20251201_JWT.question1;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * V20251202_JWTutil 专门的单元测试
 * 用于验证V20251202_JWTutil类的功能是否正常
 */
public class V20251202_JWTutilTest {

    @Test
    public void testGenerateToken() {
        // 测试token生成
        V20251202_JWTutil jwt = new V20251202_JWTutil();
        String username = "testuser";
        String token = jwt.generateToken(username);

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
        V20251202_JWTutil jwt = new V20251202_JWTutil();
        String username = "alice";
        String token = jwt.generateToken(username);

        String extractedUsername = jwt.getUsernameFromToken(token);
        assertEquals(username, extractedUsername, "从token中提取的用户名应该正确");
    }

    @Test
    public void testGetUsernameFromInvalidToken() {
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
    }
}
