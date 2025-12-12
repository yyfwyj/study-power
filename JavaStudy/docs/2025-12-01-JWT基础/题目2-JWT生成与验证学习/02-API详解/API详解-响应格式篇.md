# API详解：响应格式篇

## 📖 目录

- [1. ResponseEntity API详解](#1-responseentity-api详解)
- [2. @ControllerAdvice API详解](#2-controlleradvice-api详解)
- [3. @ExceptionHandler API详解](#3-exceptionhandler-api详解)
- [4. HttpStatus API详解](#4-httpstatus-api详解)

---

## 1. ResponseEntity API详解

### 1.1 API基本信息

- **类名**：`org.springframework.http.ResponseEntity`
- **包路径**：`org.springframework.http`
- **泛型支持**：`ResponseEntity<T>`
- **版本要求**：Spring Framework 3.0.2+

### 1.2 作用说明

**ResponseEntity解决了什么问题**？

在Spring MVC中，默认的Controller方法返回值会被自动包装成HTTP响应。但有时需要：

- 自定义HTTP状态码
- 添加自定义响应头
- 完全控制响应内容
- 处理异常情况的响应

ResponseEntity提供了完整的HTTP响应控制能力。

**为什么需要它**：

- ✅ **状态码控制**：精确控制HTTP状态码
- ✅ **响应头管理**：添加自定义Header
- ✅ **响应体控制**：完全控制响应内容
- ✅ **类型安全**：泛型支持，类型安全
- ✅ **异常处理**：在异常处理器中构造响应

**与@ResponseBody的区别**：

```java
// @ResponseBody：只控制响应体，状态码默认200
@GetMapping("/user")
@ResponseBody
public User getUser() {
    return user; // 自动转换为JSON，状态码200
}

// ResponseEntity：完全控制HTTP响应
@GetMapping("/user")
public ResponseEntity<User> getUser() {
    return ResponseEntity.ok(user); // 可控制状态码、Header等
}
```

### 1.3 使用场景

**典型使用场景**：

1. **统一响应格式**
   ```java
   @GetMapping("/user/{id}")
   public ResponseEntity<Result<User>> getUser(@PathVariable Long id) {
       User user = userService.getUser(id);
       return ResponseEntity.ok(Result.success(user));
   }
   ```

2. **异常响应**
   ```java
   @ExceptionHandler(UserNotFoundException.class)
   public ResponseEntity<Result<Void>> handleUserNotFound() {
       return ResponseEntity.status(HttpStatus.NOT_FOUND)
           .body(Result.fail(404, "用户不存在"));
   }
   ```

3. **文件下载**
   ```java
   @GetMapping("/download")
   public ResponseEntity<byte[]> downloadFile() {
       byte[] fileContent = fileService.getFileContent();
       return ResponseEntity.ok()
           .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=file.txt")
           .body(fileContent);
   }
   ```

4. **重定向**
   ```java
   @GetMapping("/old-path")
   public ResponseEntity<Void> redirect() {
       return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY)
           .header(HttpHeaders.LOCATION, "/new-path")
           .build();
   }
   ```

### 1.4 API详解

#### 静态工厂方法：ok()

**方法签名**：
```java
public static <T> ResponseEntity.BodyBuilder ok()
```

**作用**：创建200 OK响应构建器

**返回值**：`ResponseEntity.BodyBuilder` - 用于继续构建响应

**使用示例**：
```java
// 简单使用
return ResponseEntity.ok(user);

// 带Header
return ResponseEntity.ok()
    .header("Custom-Header", "value")
    .body(user);
```

#### 静态工厂方法：status()

**方法签名**：
```java
public static ResponseEntity.BodyBuilder status(HttpStatus status)
public static ResponseEntity.BodyBuilder status(int status)
```

**作用**：创建指定状态码的响应构建器

**参数说明**：

| 参数 | 类型 | 说明 |
|------|------|------|
| `status` | `HttpStatus` 或 `int` | HTTP状态码 |

**使用示例**：
```java
// 使用HttpStatus枚举
return ResponseEntity.status(HttpStatus.CREATED)
    .body(createdUser);

// 使用状态码数字
return ResponseEntity.status(201)
    .body(createdUser);
```

#### 静态工厂方法：badRequest()

**方法签名**：
```java
public static ResponseEntity.BodyBuilder badRequest()
```

**作用**：创建400 Bad Request响应构建器

**使用示例**：
```java
return ResponseEntity.badRequest()
    .body(Result.fail("参数错误"));
```

#### 其他常用工厂方法

```java
// 201 Created
ResponseEntity.created(location)

// 204 No Content
ResponseEntity.noContent()

// 404 Not Found
ResponseEntity.notFound()

// 500 Internal Server Error
ResponseEntity.internalServerError()
```

#### BodyBuilder接口方法

**header()方法**：

```java
BodyBuilder header(String headerName, String... headerValues)
```

**作用**：添加响应头

```java
return ResponseEntity.ok()
    .header("Content-Type", "application/json")
    .header("X-Custom-Header", "value")
    .body(data);
```

**headers()方法**：

```java
BodyBuilder headers(HttpHeaders headers)
```

**作用**：批量设置响应头

```java
HttpHeaders headers = new HttpHeaders();
headers.add("Content-Type", "application/json");
headers.add("X-API-Version", "1.0");

return ResponseEntity.ok()
    .headers(headers)
    .body(data);
```

**body()方法**：

```java
<T> ResponseEntity<T> body(T body)
```

**作用**：设置响应体

```java
return ResponseEntity.ok()
    .body(userData);
```

**build()方法**：

```java
ResponseEntity<Void> build()
```

**作用**：构建不带响应体的ResponseEntity

```java
// 204 No Content
return ResponseEntity.noContent().build();

// 重定向
return ResponseEntity.status(HttpStatus.FOUND)
    .header(HttpHeaders.LOCATION, "/redirect-url")
    .build();
```

### 1.5 完整案例代码

#### 案例1：统一API响应格式

```java
@RestController
@RequestMapping("/api/user")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    /**
     * 获取用户列表
     */
    @GetMapping
    public ResponseEntity<Result<List<User>>> getUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(Result.success(users));
    }
    
    /**
     * 创建用户
     */
    @PostMapping
    public ResponseEntity<Result<User>> createUser(@RequestBody CreateUserRequest request) {
        User user = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(Result.success(user));
    }
    
    /**
     * 获取单个用户
     */
    @GetMapping("/{id}")
    public ResponseEntity<Result<User>> getUser(@PathVariable Long id) {
        try {
            User user = userService.getUser(id);
            return ResponseEntity.ok(Result.success(user));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Result.fail(404, "用户不存在"));
        }
    }
    
    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Result<Void>> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
```

#### 案例2：文件下载响应

```java
@RestController
@RequestMapping("/api/files")
public class FileController {
    
    @GetMapping("/download/{filename}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String filename) {
        try {
            byte[] fileContent = fileService.getFileContent(filename);
            String contentType = fileService.getContentType(filename);
            
            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, 
                       "attachment; filename=\"" + filename + "\"")
                .header(HttpHeaders.CONTENT_TYPE, contentType)
                .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(fileContent.length))
                .body(fileContent);
                
        } catch (FileNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
```

#### 案例3：分页响应

```java
@RestController
@RequestMapping("/api/posts")
public class PostController {
    
    @GetMapping
    public ResponseEntity<Result<Page<Post>>> getPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Page<Post> posts = postService.getPosts(PageRequest.of(page, size));
        
        // 添加分页信息到Header
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(posts.getTotalElements()));
        headers.add("X-Total-Pages", String.valueOf(posts.getTotalPages()));
        headers.add("X-Current-Page", String.valueOf(posts.getNumber()));
        
        return ResponseEntity.ok()
            .headers(headers)
            .body(Result.success(posts));
    }
}
```

#### 案例4：异常处理器中的使用

```java
@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Result<Void>> handleBusinessException(BusinessException e) {
        HttpStatus status = mapToHttpStatus(e.getErrorCode());
        
        return ResponseEntity.status(status)
            .header("X-Error-Code", String.valueOf(e.getErrorCode()))
            .body(Result.fail(e.getErrorCode(), e.getMessage()));
    }
    
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Result<Void>> handleValidationException(ValidationException e) {
        return ResponseEntity.badRequest()
            .header("X-Validation-Error", "true")
            .body(Result.fail(400, e.getMessage()));
    }
    
    private HttpStatus mapToHttpStatus(int errorCode) {
        switch (errorCode) {
            case 1001: return HttpStatus.BAD_REQUEST;
            case 2001: return HttpStatus.UNAUTHORIZED;
            case 3001: return HttpStatus.FORBIDDEN;
            default: return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
}
```

### 1.6 注意事项

1. **状态码选择**
   - 200系列：成功
   - 400系列：客户端错误
   - 500系列：服务器错误

2. **响应头设置**
   - Content-Type默认application/json
   - 自定义Header使用header()方法
   - 多个同名Header使用可变参数

3. **性能考虑**
   - 大文件下载使用StreamingResponseBody
   - 避免在响应中包含敏感信息

4. **类型安全**
   - 使用泛型指定响应体类型
   - IDE会提供类型检查

---

## 2. @ControllerAdvice API详解

### 2.1 API基本信息

- **注解名**：`@ControllerAdvice`
- **包路径**：`org.springframework.web.bind.annotation`
- **版本要求**：Spring Framework 4.0+

### 2.2 作用说明

**@ControllerAdvice解决了什么问题**？

在Spring MVC应用中，异常处理通常分散在各个Controller中：

```java
// ❌ 分散的异常处理
@RestController
public class UserController {
    @GetMapping("/user/{id}")
    public User getUser(@PathVariable Long id) {
        try {
            return userService.getUser(id);
        } catch (UserNotFoundException e) {
            return null; // 处理异常
        }
    }
}

@RestController
public class OrderController {
    @PostMapping("/order")
    public Order createOrder(@RequestBody Order order) {
        try {
            return orderService.createOrder(order);
        } catch (ValidationException e) {
            return null; // 重复的异常处理
        }
    }
}
```

@ControllerAdvice提供了全局异常处理机制，将所有Controller的异常处理集中管理。

**为什么需要它**：

- ✅ **统一处理**：所有异常在一个地方处理
- ✅ **代码整洁**：Controller专注于业务逻辑
- ✅ **一致性**：统一的异常响应格式
- ✅ **可维护性**：异常处理逻辑集中管理

### 2.3 使用场景

**典型使用场景**：

1. **全局异常处理**
   ```java
   @ControllerAdvice
   public class GlobalExceptionHandler {
       @ExceptionHandler(Exception.class)
       public ResponseEntity<Result<Void>> handleException(Exception e) {
           return ResponseEntity.internalServerError()
               .body(Result.fail("系统异常"));
       }
   }
   ```

2. **业务异常处理**
   ```java
   @ExceptionHandler(BusinessException.class)
   public ResponseEntity<Result<Void>> handleBusinessException(BusinessException e) {
       return ResponseEntity.badRequest()
           .body(Result.fail(e.getCode(), e.getMessage()));
   }
   ```

3. **特定Controller的异常处理**
   ```java
   @ControllerAdvice("com.example.controller")
   public class UserExceptionHandler {
       // 只处理UserController的异常
   }
   ```

### 2.4 API详解

#### 注解属性

**basePackages属性**：

```java
@ControllerAdvice(basePackages = "com.example.controller")
```

**作用**：指定只处理特定包下的Controller异常

**使用示例**：
```java
@ControllerAdvice(basePackages = {"com.example.user", "com.example.admin"})
public class UserAdminExceptionHandler {
    // 只处理user和admin包下的异常
}
```

**annotations属性**：

```java
@ControllerAdvice(annotations = RestController.class)
```

**作用**：指定只处理带有特定注解的Controller

### 2.5 完整案例代码

#### 案例1：基础全局异常处理

```java
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    
    /**
     * 处理所有未捕获的异常
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result<Void>> handleException(Exception e) {
        log.error("系统异常", e);
        return ResponseEntity.internalServerError()
            .body(Result.fail(500, "系统异常，请联系管理员"));
    }
    
    /**
     * 处理业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Result<Void>> handleBusinessException(BusinessException e) {
        log.warn("业务异常: {}", e.getMessage());
        return ResponseEntity.badRequest()
            .body(Result.fail(e.getCode(), e.getMessage()));
    }
    
    /**
     * 处理参数验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Result<Void>> handleValidationException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .collect(Collectors.joining(", "));
        
        return ResponseEntity.badRequest()
            .body(Result.fail(400, "参数验证失败: " + message));
    }
}
```

#### 案例2：JWT相关的异常处理

```java
@ControllerAdvice
@Slf4j
public class SecurityExceptionHandler {
    
    /**
     * 处理JWT认证异常
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Result<Void>> handleAuthenticationException(AuthenticationException e) {
        log.warn("认证失败: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(Result.fail(401, "认证失败"));
    }
    
    /**
     * 处理权限不足异常
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Result<Void>> handleAccessDeniedException(AccessDeniedException e) {
        log.warn("权限不足: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body(Result.fail(403, "权限不足"));
    }
    
    /**
     * 处理Token过期异常
     */
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<Result<Void>> handleExpiredJwtException(ExpiredJwtException e) {
        log.warn("Token已过期: {}", e.getToken());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(Result.fail(401, "Token已过期，请重新登录"));
    }
    
    /**
     * 处理Token格式错误
     */
    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<Result<Void>> handleMalformedJwtException(MalformedJwtException e) {
        log.warn("Token格式错误: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(Result.fail(401, "Token格式错误"));
    }
}
```

#### 案例3：企业级异常处理器

```java
@ControllerAdvice
@Slf4j
public class EnterpriseExceptionHandler {
    
    @Autowired
    private ErrorLogService errorLogService;
    
    /**
     * 处理所有异常的统一入口
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result<Void>> handleAllExceptions(Exception e, 
                                                          HttpServletRequest request,
                                                          HttpServletResponse response) {
        
        // 记录错误日志
        ErrorLog errorLog = ErrorLog.builder()
            .url(request.getRequestURI())
            .method(request.getMethod())
            .userAgent(request.getHeader("User-Agent"))
            .clientIp(getClientIp(request))
            .exceptionClass(e.getClass().getName())
            .exceptionMessage(e.getMessage())
            .timestamp(System.currentTimeMillis())
            .build();
        
        errorLogService.saveErrorLog(errorLog);
        
        // 根据异常类型返回不同响应
        if (e instanceof BusinessException) {
            BusinessException be = (BusinessException) e;
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Result.fail(be.getCode(), be.getMessage()));
        }
        
        if (e instanceof AuthenticationException) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Result.fail(401, "认证失败"));
        }
        
        // 未知异常
        log.error("未处理的异常", e);
        return ResponseEntity.internalServerError()
            .body(Result.fail(500, "系统异常"));
    }
    
    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
```

### 2.6 注意事项

1. **异常处理顺序**
   - 具体异常在前，通用异常在后
   - 子类异常在前，父类异常在后

2. **日志记录**
   - 记录异常信息，便于排查问题
   - 不要记录敏感信息

3. **性能影响**
   - 异常处理本身有性能开销
   - 尽量避免频繁抛出异常

4. **作用域**
   - 默认全局生效
   - 可以通过basePackages限制范围

---

## 3. @ExceptionHandler API详解

### 3.1 API基本信息

- **注解名**：`@ExceptionHandler`
- **包路径**：`org.springframework.web.bind.annotation`
- **版本要求**：Spring Framework 3.0+

### 3.2 作用说明

**@ExceptionHandler定义了如何处理特定类型的异常**。

**为什么需要它**：

- ✅ **类型匹配**：处理特定类型的异常
- ✅ **精确控制**：不同异常不同处理方式
- ✅ **灵活性**：可以访问异常对象和请求信息

### 3.3 API详解

#### 注解属性

**value属性**：

```java
@ExceptionHandler({IllegalArgumentException.class, NullPointerException.class})
```

**作用**：指定要处理的异常类型，可以是多个

#### 方法参数

**支持的参数类型**：

```java
@ExceptionHandler(BusinessException.class)
public ResponseEntity<Result<Void>> handleBusinessException(
    BusinessException e,                    // 异常对象
    HttpServletRequest request,             // 请求对象
    HttpServletResponse response,           // 响应对象
    HttpSession session,                    // Session对象
    WebRequest webRequest,                  // Web请求对象
    Locale locale                           // 区域信息
) {
    // 处理逻辑
}
```

### 3.4 完整案例代码

#### 案例1：参数异常处理

```java
@ExceptionHandler(MethodArgumentNotValidException.class)
public ResponseEntity<Result<Void>> handleValidationException(
        MethodArgumentNotValidException e, 
        HttpServletRequest request) {
    
    // 收集所有字段错误
    Map<String, String> errors = new HashMap<>();
    e.getBindingResult().getFieldErrors().forEach(error -> {
        errors.put(error.getField(), error.getDefaultMessage());
    });
    
    // 记录请求信息
    log.warn("参数验证失败 - URL: {}, Method: {}, Errors: {}", 
             request.getRequestURI(), request.getMethod(), errors);
    
    return ResponseEntity.badRequest()
        .body(Result.fail(400, "参数验证失败: " + errors.toString()));
}
```

#### 案例2：数据库异常处理

```java
@ExceptionHandler(DataIntegrityViolationException.class)
public ResponseEntity<Result<Void>> handleDataIntegrityViolation(
        DataIntegrityViolationException e,
        HttpServletRequest request) {
    
    log.error("数据完整性约束违反 - URL: {}, SQL: {}", 
              request.getRequestURI(), e.getMostSpecificCause().getMessage());
    
    // 根据具体约束类型返回不同错误信息
    String message = "数据操作失败";
    if (e.getMessage().contains("Duplicate entry")) {
        message = "数据已存在";
    } else if (e.getMessage().contains("foreign key constraint")) {
        message = "关联数据不存在";
    }
    
    return ResponseEntity.badRequest()
        .body(Result.fail(400, message));
}
```

### 3.5 注意事项

1. **异常继承关系**
   - 子类异常会被优先匹配
   - 父类异常处理更通用的异常

2. **返回值类型**
   - 可以返回ModelAndView
   - 可以返回ResponseEntity
   - 可以返回String（视图名）

3. **方法可见性**
   - 必须是public方法

---

## 4. HttpStatus API详解

### 4.1 API基本信息

- **枚举类**：`org.springframework.http.HttpStatus`
- **包路径**：`org.springframework.http`
- **版本要求**：Spring Framework 3.0+

### 4.2 作用说明

**HttpStatus枚举了所有标准的HTTP状态码**。

**为什么需要它**：

- ✅ **类型安全**：避免硬编码数字状态码
- ✅ **可读性**：状态码有意义的名字
- ✅ **标准性**：符合HTTP协议标准

### 4.3 常用状态码

**成功状态码**：

```java
HttpStatus.OK                    // 200
HttpStatus.CREATED               // 201
HttpStatus.NO_CONTENT            // 204
```

**客户端错误**：

```java
HttpStatus.BAD_REQUEST           // 400
HttpStatus.UNAUTHORIZED          // 401
HttpStatus.FORBIDDEN             // 403
HttpStatus.NOT_FOUND             // 404
HttpStatus.CONFLICT              // 409
```

**服务器错误**：

```java
HttpStatus.INTERNAL_SERVER_ERROR // 500
HttpStatus.BAD_GATEWAY           // 502
HttpStatus.SERVICE_UNAVAILABLE   // 503
```

### 4.4 API详解

#### 核心方法

**value()方法**：

```java
int value()  // 获取状态码数字值
```

**使用示例**：

```java
HttpStatus status = HttpStatus.OK;
int code = status.value(); // 200
```

**isError()方法**：

```java
boolean isError()  // 是否为错误状态码
```

**使用示例**：

```java
HttpStatus.BAD_REQUEST.isError(); // true
HttpStatus.OK.isError();          // false
```

### 4.5 完整案例代码

#### 案例1：在异常处理器中使用

```java
@ControllerAdvice
public class GlobalExceptionHandler {
    
    // 业务状态码到HTTP状态码的映射
    private HttpStatus mapBusinessCodeToHttpStatus(int businessCode) {
        switch (businessCode) {
            case 2001: return HttpStatus.UNAUTHORIZED;    // 未登录
            case 3001: return HttpStatus.FORBIDDEN;       // 无权限
            case 4001: return HttpStatus.NOT_FOUND;       // 数据不存在
            case 5001: return HttpStatus.INTERNAL_SERVER_ERROR; // 系统错误
            default: return HttpStatus.BAD_REQUEST;       // 默认400
        }
    }
    
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Result<Void>> handleBusinessException(BusinessException e) {
        HttpStatus httpStatus = mapBusinessCodeToHttpStatus(e.getCode());
        
        return ResponseEntity.status(httpStatus)
            .body(Result.fail(e.getCode(), e.getMessage()));
    }
}
```

#### 案例2：动态状态码选择

```java
public ResponseEntity<Result<Void>> handleServiceException(ServiceException e) {
    HttpStatus status;
    
    // 根据异常类型选择状态码
    if (e.isClientError()) {
        status = e.isNotFound() ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
    } else if (e.isServerError()) {
        status = e.isTimeout() ? HttpStatus.REQUEST_TIMEOUT : HttpStatus.INTERNAL_SERVER_ERROR;
    } else {
        status = HttpStatus.INTERNAL_SERVER_ERROR;
    }
    
    return ResponseEntity.status(status)
        .body(Result.fail(e.getCode(), e.getMessage()));
}
```

### 4.6 注意事项

1. **状态码含义**
   - 理解每个状态码的语义含义
   - 不要滥用状态码

2. **客户端兼容性**
   - 不同客户端对状态码的支持可能不同
   - 优先使用标准状态码

3. **扩展状态码**
   - HTTP允许自定义状态码（例如4xx范围）
   - 但尽量使用标准状态码

---

## 📝 总结

### 关键API记忆

1. **ResponseEntity** - HTTP响应控制
   - `ResponseEntity.ok()` - 200响应
   - `ResponseEntity.status()` - 自定义状态码
   - `header()` - 添加响应头
   - `body()` - 设置响应体

2. **@ControllerAdvice** - 全局异常处理
   - 统一处理所有Controller异常
   - 支持指定包或注解范围

3. **@ExceptionHandler** - 异常处理方法
   - 指定处理的具体异常类型
   - 可以访问异常和请求信息

4. **HttpStatus** - HTTP状态码枚举
   - 类型安全的状态码表示
   - 丰富的状态码常量

### 最佳实践

- ✅ 使用统一的响应格式
- ✅ 全局异常处理集中管理
- ✅ 正确选择HTTP状态码
- ✅ 详细的错误日志记录

---

*文档更新时间：2025年12月2日*

