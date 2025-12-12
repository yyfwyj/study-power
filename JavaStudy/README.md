# JavaStudy - JWT学习项目

## 项目简介

这是一个基于Spring Boot的Java学习项目，专注于JWT（JSON Web Token）技术的学习和实践。

## 项目结构

```
JavaStudy/
├── src/
│   ├── main/
│   │   ├── java/com/javaseudy/
│   │   │   ├── JavaStudyApplication.java    # Spring Boot主应用类
│   │   │   ├── jwt/
│   │   │   │   ├── question1/               # 第一题代码目录
│   │   │   │   ├── question2/               # 第二题代码目录
│   │   │   │   └── question3/               # 第三题代码目录
│   │   └── resources/
│   │       └── application.properties       # 应用配置文件
│   └── test/java/                           # 测试类
├── docs/
│   ├── 2025-12-01-JWT基础/                  # 按日期主题组织的学习文档
│   │   ├── 题目1-JWT基本概念.md             # 第一题学习笔记
│   │   ├── 题目2-JWT生成与验证.md           # 第二题学习笔记
│   │   ├── 题目3-JWT在SpringBoot中的应用.md # 第三题学习笔记
│   │   └── 知识点总结.md                    # 总结文档
│   └── templates/
│       └── learning-template.md             # 学习模板
├── pom.xml                                  # Maven配置文件
└── README.md                                # 项目说明文档
```

## JWT学习计划

### 题目1：基础实现 —— 实现 JWT 工具类（入门）
- 掌握 JWT 的基本结构和生成/解析方法
- 使用 HS512 算法，2小时过期时间
- 实现 `generateToken()` 和 `getUsernameFromToken()` 方法

### 题目2：集成 Spring Security —— 自定义 JWT 认证过滤器（进阶）
- 创建 `JwtAuthenticationFilter` 继承 `OncePerRequestFilter`
- 从 Authorization 头提取 Bearer token
- 自动验证 token 并设置认证上下文

### 题目3：安全加固 —— 防止算法混淆攻击（高阶）
- 理解 CVE-2016-10555 漏洞
- 使用 `parserBuilder()` 显式指定 HS512 算法
- 防止 alg 头被篡改的攻击

## 技术栈

- Java 11
- Spring Boot 2.7.0
- Spring Security
- JWT (JJWT库)
- Maven

## 快速开始

1. 确保安装了Java 11和Maven
2. 克隆或下载项目
3. 在项目根目录运行：`mvn spring-boot:run`
4. 访问 http://localhost:8080

## 学习资源

- [JWT官网](https://jwt.io/)
- [Spring Security文档](https://spring.io/projects/spring-security)
- [B站JWT教学视频](https://www.bilibili.com/video/BV1fEUpBEEQ5/)

## 注意事项

- JWT密钥请妥善保管，不要在生产环境中使用默认密钥
- Token有过期时间，需要实现刷新机制
- 注意XSS和CSRF攻击防护
