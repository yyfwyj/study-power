package com.javastudy.V20251201_JWT.question1;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;

public class jwtUtil {
    // 密钥
    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor("MySuperSecretKeyForJWT!@#1234567890".getBytes());
    // Token有效期
    private static final long EXPIRATION_TIME = 7200000;


    /**
     * 生成Token
     *
     * @param sub 用户名
     * @return Token令牌
     */
    public String generateToken(String sub) {
        String token = Jwts.builder()
                // .setSubject(String sub) → 设置用户身份
                // 作用：告诉服务器“这个 Token 是谁的”
                // 通常存什么：用户 ID、用户名、手机号（但别存密码！）
                .setSubject(sub)
                // .setExpiration(Date exp) → 设置过期时间
                // Token有效期 = 当前系统时间毫秒数 + Token 指定的有效期毫秒数
                // 作用：Token 多久后失效？防止被人偷了长期用。
                // 必须设置！ 否则 Token 永不过期，很危险。
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                // .signWith(Key key, SignatureAlgorithm alg) → 签名（最关键！）
                // 作用：给 Token 加“防伪印章”，防止别人伪造或篡改。
                // 两个参数：
                // key：你的密钥（必须保密！不能泄露）
                // alg：用什么算法盖章（一般用 HS256）
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                // .claim(String name, Object value) → 加自定义信息
                // 自定义信息
                // 作用：除了用户ID，还想带点别的？比如角色、部门、权限等。
                // name：字段名（你自己定）
                // value：值（支持 String、Number、List、Map 等）
                .claim("demo", "hello jwt")
                // .setIssuer(String iss) → 设置签发者
                // 设置签发者
                // 作用：标明这个 Token 是谁发的（比如你的系统名字）
                // 用途：多系统联调时，知道来源；安全审计也有用。
                .setIssuer("jwtDemo")
                // .setIssuedAt(Date iat) → 设置签发时间
                // 作用：记录 Token 是什么时候生成的。一般不用手动设，因为 .compact() 时会自动填当前时间。如果你非要指定：
                //  .setId(String jti) → 设置唯一ID
                // 设置唯一ID
                // 作用：给每个 Token 一个全球唯一的编号（类似订单号）。
                // 用途：用于日志追踪、防止重放攻击（高级用法）。
                .setId(UUID.randomUUID().toString())
                // .setAudience(String... aud) → 设置接收方
                // 作用：这个 Token 是给谁用的？（比如只给“移动端”用）
                // .setAudience("web-client", "api-gateway") // 支持多个
                .setAudience("web-client")
                // .setNotBefore(Date nbf)  → 设置生效时间
                // 作用：Token 在某个时间之前不能用
                // 例子：
                // 10分钟后才生效
                // .setNotBefore(new Date(System.currentTimeMillis() + 600_000))
                // .compressWith(CompressionCodes codec) → 压缩 Token（高级）
                // 作用：如果 Payload 过大，可以压缩一下，让 Token 占用更少的空间。
                // 注意：压缩后 Token 会变短，但解析时要对应解压。
                // .compressWith(CompressionCodecs.GZIP)
                // .compact() → 生成最终字符串！
                // 作用：把前面所有设置打包成一个完整的 JWT 字符串。
                // 必须调用！ 否则你得不到 Token。
                // 返回值：String，就是你要返回给前端的那个 Token。
                .compact();

        return token;
    }

    public String getUsernameFromToken(String token) {
        try {
            // parserBuilder() 是干啥的？
            // Jwts.parserBuilder() 返回一个 JwtParserBuilder 实例，该实例用于逐步配置 JWT 解析所需的参数，
            // 例如签名密钥、时钟偏差容忍度、是否启用压缩等。
            // 最终通过调用 .build() 方法生成一个线程安全、不可变的 JwtParser 对象，用于实际解析 JWT 字符串。
            Claims claims = Jwts.parserBuilder()
                    // setSigningKey(byte[] key) / setSigningKey(String base64EncodedKeyBytes)
                    // 作用：设置用于验证 JWT 签名的密钥。
                    .setSigningKey(SECRET_KEY)

                    // 以下都属于声明（Claims）：强制校验，若不符则抛出 InvalidClaimException。
                    // requireIssuer(String issuer)
                    // 作用：要求 iss（签发者）必须等于指定值。
                    // 场景：微服务 A 只信任由认证中心 auth.example.com 签发的 Token。
                    // .requireIssuer("jwtDemo")

                    // requireSubject(String subject)
                    // 作用：要求 sub（主体）等于指定值。
                    // 场景：极少用，通常由业务逻辑判断。

                    // requireAudience(String audience)
                    // 作用：要求 aud包含指定值（支持多个）。
                    // 场景：API 网关只接受 aud=api-gateway 的 Token。
                    // .requireAudience("user-service", "admin-panel")

                    // require(String claimName, Object value)
                    // 作用：通用方法，校验任意自定义声明。
                    // 场景：校验 tenantId="companyA"。
                    // .require("tenantId", "acme-corp")

                    // setAllowedClockSkewSeconds(long seconds)
                    // 作用：允许系统时间存在 seconds 秒的偏差（用于容忍服务器时钟不同步）。
                    // 默认：0 秒（严格校验）。
                    // 场景：分布式系统中各节点时间略有差异。
                    // .setAllowedClockSkewSeconds(60) // 允许 1 分钟偏差
                    .build()
                    // parseClaimsJws + getBody = ：把一个加密的“通行证”（JWT）拆开，拿出里面写的“你是谁、有什么权限、什么时候过期”这些信息。
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject();
        }
        // ExpiredJwtException	exp（过期时间）早于当前时间	Token 合法但已失效
        catch (ExpiredJwtException e)  {
            return "Token 已过期";
        }
        // SignatureException 签名验证失败（密钥错误或者是Token被篡改）
        catch (SignatureException e) {
            return "Token 签名验证失败";
        }
        // MalformedJwtException JWT格式不符合 header.payload.signature 三段结构
        catch (MalformedJwtException e) {
            return "Token 格式错误";
        }
        // UnsupportedJwtException 不支持的 Token 格式 使用了 JJWT 不支持的 JWT 类型（如加密型 JWE）
        catch (UnsupportedJwtException e) {
            return "Token 不支持";
        }
        // Token 为 null/空，或密钥长度不足（<32字节 for HS256） 或者是不符合校验规则
        catch (IllegalArgumentException e) {
            return "Token 参数错误";
        }

    }
}
