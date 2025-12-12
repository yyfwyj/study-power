# JWT第二题：JWT生成与验证 - 学习导航

## 📚 学习路径导航

本目录包含JWT第二题的完整学习资料，按照学习阶段组织，便于循序渐进地掌握知识点。

### 🗂️ 目录结构说明

```
题目2-JWT生成与验证学习/
├── README.md                              # 本文件 - 学习指南索引
├── 01-学习指南/
│   └── 完整学习指南.md                    # 主学习路径和步骤指导
├── 02-API详解/
│   ├── API详解-过滤器篇.md                # 过滤器相关API详解
│   ├── API详解-SpringSecurity篇.md        # Spring Security API详解
│   └── API详解-响应格式篇.md              # 响应格式API详解
├── 03-理论基础/
│   ├── 理论基础-过滤器篇.md               # 过滤器原理与设计
│   ├── 理论基础-SpringSecurity篇.md       # Spring Security核心概念
│   └── 理论基础-响应格式篇.md             # 统一响应格式设计
└── 04-测试验证/
    └── 测试场景与验证.md                  # 测试场景说明
```

## 🎯 学习目标

通过本阶段学习，你将掌握：

1. **Token过滤和验证** - 理解过滤器的设计思路和实现方法
2. **Spring Security集成** - 掌握Spring Security的核心概念和配置方法
3. **统一响应格式** - 学会设计企业级的API响应格式和异常处理

## 📖 推荐学习顺序

### 第一阶段：理解基础概念（约1-2小时）

1. **阅读理论基础文档**
   - [理论基础-过滤器篇](./03-理论基础/理论基础-过滤器篇.md) - 理解过滤器的工作原理
   - [理论基础-SpringSecurity篇](./03-理论基础/理论基础-SpringSecurity篇.md) - 理解Spring Security核心概念
   - [理论基础-响应格式篇](./03-理论基础/理论基础-响应格式篇.md) - 理解响应格式设计原则

### 第二阶段：掌握API使用（约2-3小时）

2. **深入学习API详解**
   - [API详解-过滤器篇](./02-API详解/API详解-过滤器篇.md) - 掌握过滤器相关API
   - [API详解-SpringSecurity篇](./02-API详解/API详解-SpringSecurity篇.md) - 掌握Spring Security API
   - [API详解-响应格式篇](./02-API详解/API详解-响应格式篇.md) - 掌握响应格式API

### 第三阶段：实践开发（约3-4小时）

3. **跟随学习指南动手实践**
   - [完整学习指南](./01-学习指南/完整学习指南.md) - 按照步骤自己实现代码
   - 参考框架代码，填充TODO部分
   - 边学边做，加深理解

### 第四阶段：测试验证（约30分钟）

4. **验证学习成果**
   - [测试场景与验证](./04-测试验证/测试场景与验证.md) - 完整测试你的实现
   - 确保所有测试场景通过

## 🔑 关键知识点索引

### 过滤器相关
- **OncePerRequestFilter** - [API详解-过滤器篇 - OncePerRequestFilter](./02-API详解/API详解-过滤器篇.md#onceperrequestfilter-api详解)
- **HttpServletRequest** - [API详解-过滤器篇 - HttpServletRequest](./02-API详解/API详解-过滤器篇.md#httpservletrequest-api详解)
- **FilterChain** - [API详解-过滤器篇 - FilterChain](./02-API详解/API详解-过滤器篇.md#filterchain-api详解)

### Spring Security相关
- **SecurityContextHolder** - [API详解-SpringSecurity篇 - SecurityContextHolder](./02-API详解/API详解-SpringSecurity篇.md#securitycontextholder-api详解)
- **UsernamePasswordAuthenticationToken** - [API详解-SpringSecurity篇 - UsernamePasswordAuthenticationToken](./02-API详解/API详解-SpringSecurity篇.md#usernamepasswordauthenticationtoken-api详解)
- **HttpSecurity** - [API详解-SpringSecurity篇 - HttpSecurity](./02-API详解/API详解-SpringSecurity篇.md#httpsecurity-api详解)

### 响应格式相关
- **ResponseEntity** - [API详解-响应格式篇 - ResponseEntity](./02-API详解/API详解-响应格式篇.md#responseentity-api详解)
- **@ControllerAdvice** - [API详解-响应格式篇 - @ControllerAdvice](./02-API详解/API详解-响应格式篇.md#controlleradvice-api详解)
- **HttpStatus** - [API详解-响应格式篇 - HttpStatus](./02-API详解/API详解-响应格式篇.md#httpstatus-api详解)

## 📝 学习建议

1. **不要急于看代码** - 先理解理论，再动手实践
2. **边学边做** - 看完API详解后，立即尝试使用
3. **多思考为什么** - 理解设计思路，而不只是记住代码
4. **测试驱动** - 每完成一部分，就进行测试验证
5. **复习巩固** - 完成实现后，回看理论基础，加深理解

## ❓ 遇到问题？

如果在学习过程中遇到问题：

1. **先查看API详解** - 每个API都有详细的使用说明和案例
2. **参考理论基础** - 理解原理有助于解决问题
3. **查看学习指南** - 里面有常见问题的解答
4. **对比测试场景** - 看看测试场景是否覆盖你的问题

## 🎓 学习检查清单

完成学习后，检查你是否掌握：

- [ ] 理解过滤器的工作原理和在Spring Security中的位置
- [ ] 能够实现JWT Token的提取和验证
- [ ] 理解SecurityContext的作用和使用方法
- [ ] 能够配置Spring Security的过滤器链
- [ ] 理解统一响应格式的设计原则
- [ ] 能够实现全局异常处理
- [ ] 所有测试场景都能通过

---

*最后更新时间：2025年12月2日*
*学习建议时长：6-8小时*
