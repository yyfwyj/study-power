package com.javastudy.V20251201_JWT.question2.common;

/**
 * 统一响应格式类
 * 
 * 作用：封装所有API的统一响应格式
 * 
 * 设计思路：
 * - 所有接口都使用相同的响应结构
 * - 包含状态码、消息、数据、时间戳
 * - 提供静态工厂方法，便于使用
 * - 支持泛型，可以返回不同类型的数据
 * 
 * 学习重点：
 * - 统一响应格式的重要性
 * - 泛型的使用
 * - 静态工厂方法的设计模式
 * - HTTP状态码 vs 业务状态码
 * 
 * @param <T> 响应数据的类型
 */
public class Result<T> {
    
    /**
     * 状态码
     * 
     * 说明：
     * - 通常使用HTTP状态码：200、400、401、403、500等
     * - 也可以使用业务状态码：2001、2002等
     * - 根据项目需求选择
     */
    private Integer code;
    
    /**
     * 消息描述
     * 
     * 说明：
     * - 成功时：通常为"success"或"操作成功"
     * - 失败时：描述具体的错误信息
     * - 便于前端显示和调试
     */
    private String message;
    
    /**
     * 响应数据
     * 
     * 说明：
     * - 成功时：包含业务数据
     * - 失败时：通常为null
     * - 使用泛型，支持不同类型的数据
     */
    private T data;
    
    /**
     * 时间戳
     * 
     * 说明：
     * - 响应生成的时间（毫秒）
     * - 便于日志记录和问题排查
     */
    private Long timestamp;
    
    /**
     * 无参构造函数
     * 
     * 作用：用于JSON反序列化
     */
    public Result() {
        this.timestamp = System.currentTimeMillis();
    }
    
    /**
     * 全参构造函数
     * 
     * @param code 状态码
     * @param message 消息
     * @param data 数据
     * @param timestamp 时间戳
     */
    public Result(Integer code, String message, T data, Long timestamp) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = timestamp;
    }
    
    /**
     * 静态工厂方法：成功响应（带数据）
     * 
     * 使用场景：
     * - 操作成功，需要返回数据
     * 
     * 示例：
     * Result<User> result = Result.success(user);
     * 
     * @param data 响应数据
     * @param <T> 数据类型
     * @return Result对象
     */
    public static <T> Result<T> success(T data) {
        // TODO: 实现成功响应
        // 提示：
        // 1. 状态码：200（成功）
        // 2. 消息："success"或"操作成功"
        // 3. 数据：传入的data参数
        // 4. 时间戳：System.currentTimeMillis()
        
        return new Result<>(200, "success", data, System.currentTimeMillis());
    }
    
    /**
     * 静态工厂方法：成功响应（无数据）
     * 
     * 使用场景：
     * - 操作成功，不需要返回数据
     * 
     * 示例：
     * Result<Void> result = Result.success();
     * 
     * @param <T> 数据类型
     * @return Result对象
     */
    public static <T> Result<T> success() {
        // TODO: 实现成功响应（无数据）
        // 提示：data传null即可
        
        return success(null);
    }
    
    /**
     * 静态工厂方法：失败响应
     * 
     * 使用场景：
     * - 操作失败，返回错误信息
     * 
     * 示例：
     * Result<Void> result = Result.fail(400, "参数错误");
     * 
     * @param code 状态码
     * @param message 错误消息
     * @param <T> 数据类型
     * @return Result对象
     */
    public static <T> Result<T> fail(Integer code, String message) {
        // TODO: 实现失败响应
        // 提示：
        // 1. 状态码：传入的code参数
        // 2. 消息：传入的message参数
        // 3. 数据：null
        // 4. 时间戳：System.currentTimeMillis()
        
        return new Result<>(code, message, null, System.currentTimeMillis());
    }
    
    /**
     * 静态工厂方法：失败响应（使用默认状态码）
     * 
     * 使用场景：
     * - 操作失败，使用默认状态码（500）
     * 
     * 示例：
     * Result<Void> result = Result.fail("服务器内部错误");
     * 
     * @param message 错误消息
     * @param <T> 数据类型
     * @return Result对象
     */
    public static <T> Result<T> fail(String message) {
        // TODO: 实现失败响应（默认状态码500）
        // 提示：使用fail(500, message)
        
        return fail(500, message);
    }
    
    // Getter和Setter方法
    // TODO: 生成Getter和Setter方法
    // 提示：使用IDE自动生成，或者手动编写
    
    public Integer getCode() {
        return code;
    }
    
    public void setCode(Integer code) {
        this.code = code;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public T getData() {
        return data;
    }
    
    public void setData(T data) {
        this.data = data;
    }
    
    public Long getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
    
    @Override
    public String toString() {
        return "Result{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                ", timestamp=" + timestamp +
                '}';
    }
}

