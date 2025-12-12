package com.javastudy.V20251201_JWT.question1;

// 需要导入的包（已提供，便于参考和编译）：

/**
 * JWT的理论
 * 1. 为什么要使用JWT
 * 浏览器的前端和后端是通过HTTP/HTTPS协议进行前后端链接，而HTTP/HTTPS协议是无状态的
 * 那么，当服务端接收到前端请求以后，无法通过常规手段去进行判断用户是否为同一个人，或者是用户是否登录，是否有使用当前功能的权限
 * 所以就需要使用JWT
 * 2. JWT的实现原理
 * JWT，是通过三部分构成的，分拨是：header，payload，signature
 * header: JWT头部，包含了typ和alg，typ代表令牌类型，通常为“JWT”，alg是全名算放，例如HS256 HS512
 * payload：Token过期时间，前发这，以及用户相关数据
 * signature: 签名，用于验证Token的合法性
 * 3. 所以我们使用Token，一是为了去证明用户身份与请求是合法的，同时因为Token是保存在前端的
 * 所以为了避免用户纂改Token，那么还需要算法来对密钥与算法来加密信息
 */

import io.jsonwebtoken.*; // JWT核心类（Claims, Jwts, ExpiredJwtException, MalformedJwtException, UnsupportedJwtException等）
import io.jsonwebtoken.security.Keys; // 密钥工具类，用于创建SecretKey
import io.jsonwebtoken.security.SignatureException; // 签名验证异常

import javax.crypto.SecretKey; // 密钥接口
import java.util.Date; // 日期类
import java.util.UUID; // UUID生成工具

public class V20251202_JWTutil {

    // 步骤1：定义密钥常量
    // 使用 Keys.hmacShaKeyFor() 方法，将字符串密钥转换为 SecretKey 对象
    // 参数：密钥字符串的字节数组（通过 getBytes() 获取）
    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor("MySuperSecretKeyForJWT!@#1234567890".getBytes()); // TODO: 使用 Keys.hmacShaKeyFor() 创建密钥

    // 步骤2：定义Token有效期常量（单位：毫秒）
    // 例如：7200000 表示 2小时（2 * 60 * 60 * 1000）
    private static final long EXPIRATION_TIME = 7200000; // TODO: 设置有效期（毫秒）

    /**
     * 生成Token的方法
     * <p>
     * 功能：根据用户名生成JWT令牌
     *
     * @param sub 用户名（JWT中的subject字段）
     * @return Token令牌字符串
     * <p>
     * 实现步骤：
     * 1. 使用 Jwts.builder() 创建JWT构建器
     * 2. 调用 .setSubject(sub) 设置用户身份
     * 3. 调用 .setExpiration() 设置过期时间
     * - 参数：new Date(System.currentTimeMillis() + EXPIRATION_TIME)
     * - 作用：当前时间 + 有效期 = 过期时间
     * 4. 调用 .signWith() 进行签名
     * - 参数1：SECRET_KEY（密钥）
     * - 参数2：SignatureAlgorithm.HS256（签名算法）
     * 5. （可选）调用 .claim() 添加自定义信息
     * - 参数1：字段名（String）
     * - 参数2：字段值（Object）
     * 6. （可选）调用 .setIssuer() 设置签发者
     * 7. （可选）调用 .setId() 设置唯一ID
     * - 参数：UUID.randomUUID().toString()
     * 8. （可选）调用 .setAudience() 设置接收方
     * 9. 调用 .compact() 生成最终的Token字符串
     * 10. 返回Token
     */
    public String generateToken(String sub) {
        // TODO: 在这里实现Token生成逻辑

        // 提示：使用链式调用
        // Jwts.builder()
        //     .setSubject(...)
        //     .setExpiration(...)
        //     .signWith(...)
        //     .compact();

        return Jwts.builder()
                .setSubject(sub)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .claim("id", "996")
                .setIssuer("JWT")
                .setId(UUID.randomUUID().toString())
                .compact();
    }

    /**
     * 从Token中获取用户名的方法
     * <p>
     * 功能：解析JWT令牌，提取其中的用户信息
     *
     * @param token JWT令牌字符串
     * @return 用户名，如果解析失败则返回错误信息
     * <p>
     * 实现步骤：
     * 1. 使用 try-catch 包裹解析逻辑（需要捕获多种异常）
     * 2. 使用 Jwts.parserBuilder() 创建解析器构建器
     * 3. 调用 .setSigningKey(SECRET_KEY) 设置验证密钥
     * 4. 调用 .build() 构建解析器
     * 5. 调用 .parseClaimsJws(token) 解析Token（会抛出异常如果Token无效）
     * 6. 调用 .getBody() 获取Claims对象（包含Token中的信息）
     * 7. 调用 claims.getSubject() 获取用户名
     * 8. 返回用户名
     * <p>
     * 异常处理（需要捕获以下异常）：
     * - ExpiredJwtException: Token已过期
     * → 返回 "Token 已过期"
     * - SignatureException: 签名验证失败（密钥错误或Token被篡改）
     * → 返回 "Token 签名验证失败"
     * - MalformedJwtException: Token格式错误（不符合 header.payload.signature 三段结构）
     * → 返回 "Token 格式错误"
     * - UnsupportedJwtException: 不支持的Token格式
     * → 返回 "Token 不支持"
     * - IllegalArgumentException: Token参数错误（null/空/密钥长度不足等）
     * → 返回 "Token 参数错误"
     */
    public String getUsernameFromToken(String token) {
        // TODO: 在这里实现Token解析逻辑

        // 提示：使用 try-catch 结构
        // try {
//             Claims claims = Jwts.parserBuilder()
//                 .setSigningKey(...)
        //         .build()
        //         .parseClaimsJws(...)
        //         .getBody();
        //     return claims.getSubject();
        // } catch (异常类型 e) {
        //     return "错误信息";
        // }

        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject();
        } catch (ExpiredJwtException e) {
            return "Token 已过期";
        } catch (SignatureException e) {
            return "Token 签名验证失败";
        } catch (MalformedJwtException e) {
            return "Token 格式错误";
        } catch (UnsupportedJwtException e) {
            return "Token 不支持";
        } catch (IllegalArgumentException e) {
            return "Token 参数错误";
        }
    }
}

