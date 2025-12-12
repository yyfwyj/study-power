# 🧩 题目 3：安全加固 —— 防止算法混淆攻击（高阶）

## 🎯 目标
理解 JWT 安全漏洞（CVE-2016-10555）并实现防御。

## 📋 背景
某些 JWT 库如果未严格指定算法，可能被攻击者将 `alg: RS256` 改为 `alg: HS256`，并用 RSA 公钥作为 HMAC 密钥伪造 Token。

## 🔧 要求

修改你的 `JwtUtil` 类，不再使用简单的 `setSigningKey`，而是使用 **白名单算法 + 显式密钥** 的方式验证 Token。

### 具体做法：

在 `validateToken(String token)` 方法中：
- 使用 `Jwts.parserBuilder()`
- 显式只允许 `HS512` 算法
- 使用 `.setSigningKey(...)` 设置密钥
- 调用 `.build().parseClaimsJws(token)` 进行验证
- 如果 Token 的 `alg` 不是 `HS512`，应直接抛出 `SignatureException`

## 💡 提示

**jjwt 0.11.5+ 推荐使用 `parserBuilder()`**

错误写法：
```java
Jwts.parser().setSigningKey(key).parseClaimsJws(token)
```

正确写法：
```java
Jwts.parserBuilder().setSigningKey(key).requireAlgorithm(SignatureAlgorithm.HS512).build().parseClaimsJws(token)
```

## 🧪 测试思路（思考题）
如果有人把 JWT Header 的 `alg` 改成 `"none"` 或 `"RS256"`，你的代码是否能拒绝？

## 📝 实现步骤

1. **修改 JwtUtil 类**
   - 在 `src/main/java/com/javaseudy/jwt/question3/` 目录下
   - 基于题目1的代码进行修改

2. **更新 validateToken 方法**
   - 使用 `parserBuilder()`
   - 添加 `requireAlgorithm(SignatureAlgorithm.HS512)`
   - 确保只接受 HS512 算法

3. **测试安全加固**
   - 尝试使用不同算法的token
   - 验证是否会被拒绝

## 🔧 依赖配置
确保使用 JJWT 0.11.5+ 版本：
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

## 🧪 测试验证

### 1. 正常测试
```java
// 生成HS512 token
String token = JwtUtil.generateToken("alice");

// 验证应该成功
boolean valid = JwtUtil.validateToken(token);
assertTrue(valid);
```

### 2. 安全测试
```java
// 尝试伪造alg头的token（模拟攻击）
String maliciousToken = "eyJhbGciOiJub25lIiwidHlwIjoiSldUIn0.eyJzdWIiOiJhdHRhY2tlciJ9.";

// 验证应该失败并抛出异常
try {
    JwtUtil.validateToken(maliciousToken);
    fail("Should have thrown exception");
} catch (SignatureException e) {
    // 正确，应该拒绝
}
```

## 🏆 验收标准
- ✅ 正常 HS512 token 验证通过
- ✅ 伪造算法的 token 被拒绝
- ✅ 抛出适当的异常信息
- ✅ 安全性得到提升

## 📚 学习要点
- JWT 算法混淆攻击原理
- CVE-2016-10555 漏洞详情
- 白名单算法验证的重要性
- JJWT 安全最佳实践

## ⚠️ 安全提醒
- 永远不要信任客户端传来的 alg 头
- 显式指定允许的算法
- 定期更新 JWT 库版本
- 在生产环境中使用强密钥

---

*学习日期：2025年12月1日*
*题目难度：⭐⭐⭐⭐⭐*
*预计完成时间：30分钟*
