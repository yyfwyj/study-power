# 🧩 题目 1：基础实现 —— 实现 JWT 工具类（入门）

## 🎯 目标
掌握 JWT 的基本结构和生成/解析方法。

## 📋 要求

编写一个 `JwtUtil` 工具类，包含以下两个静态方法：

### `generateToken(String username)`
- 使用 **HS512** 算法
- 密钥为 `"MySuperSecretKeyForJWT!@#1234567890"`（硬编码即可）
- Token 有效期为 **2 小时**（7200000 毫秒）
- Payload 中必须包含：
  - `sub`（subject）= username
  - `iat`（签发时间）
  - `exp`（过期时间）

### `getUsernameFromToken(String token)`
- 解析 Token，返回 `sub` 字段（即用户名）
- 如果 Token 无效（签名错误、过期等），返回 `null`

## 💡 提示
使用 `io.jsonwebtoken.Jwts`、`Claims`、`SignatureAlgorithm.HS512`

## 🧪 示例测试
```java
String token = JwtUtil.generateToken("alice");
System.out.println(token); // 应输出合法 JWT

String user = JwtUtil.getUsernameFromToken(token);
System.out.println(user); // 输出 "alice"
```

## 📝 实现步骤

1. **创建 JwtUtil 类**
   - 在 `src/main/java/com/javaseudy/jwt/question1/` 目录下创建 `JwtUtil.java`

2. **实现 generateToken 方法**
   - 设置签名算法为 HS512
   - 配置密钥
   - 添加必要的 claims
   - 设置过期时间

3. **实现 getUsernameFromToken 方法**
   - 解析 JWT token
   - 提取 subject 字段
   - 处理异常情况

## 🔧 依赖配置
确保 `pom.xml` 中包含：
```xml
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.11.5</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.11.5</version>
    <scope>runtime</scope>
</dependency>
```

## 🏆 验收标准
- ✅ Token 生成成功且格式正确
- ✅ Token 解析能正确提取用户名
- ✅ 无效 token 返回 null
- ✅ 过期 token 返回 null

## 📚 学习要点
- JWT 的三部分结构：Header、Payload、Signature
- HS512 签名算法的使用
- Claims 的设置和获取
- 异常处理的重要性

---

*学习日期：2025年12月1日*
*题目难度：⭐⭐☆☆☆*
*预计完成时间：30分钟*
