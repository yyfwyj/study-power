package com.example.user.aicodehelper.ai;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j // 打印日志
public class AiCodeHelper {

    // 引入ChatModel Bean 这个就是我们通过依赖引入的大模型
    @Resource
    private ChatModel qwenChatModel;

    // 3.特性 SystemMessage 系统提示词，我们可以通过系统提示词来引导AI，设置AI身份，让AI的回答更加准确以及高效
    // 需要注意一下，系统提示词最好只有一个发送给大模型，否则新的提示词会替换旧的提示词
    private static final String SYSTEM_MESSAGE = """
                你是编程领域的小助手，帮助用户解答编程学习和求职面试相关的问题，并给出建议。重点关注 4 个方向：
                1. 规划清晰的编程学习路线
                2. 提供项目学习建议
                3. 给出程序员求职全流程指南（比如简历优化、投递技巧）
                4. 分享高频面试题和面试技巧
                请用简洁易懂的语言回答，助力用户高效学习与求职。
            """;

    // 1. 简单对话
    // 特性：ChatModel，这是一个最基本的文本聊天方法
    public String chat(String message) {
        UserMessage userMessage = UserMessage.from(message);
        ChatResponse chatResponse = qwenChatModel.chat(userMessage);
        AiMessage aiMessage = chatResponse.aiMessage();
        log.info("ai 输出:" + aiMessage.toString());
        return aiMessage.text();
    }

    // 2. 简单对话 - 自定义用户消息
    // 特性：多模态，可以支持给AI发送多种类型的消息，例如：文本、图片、音频、视频等等，但是需要知道AI本身是否支持这个功能
    public String chatWithMessage(UserMessage userMessage) {
        ChatResponse chatResponse = qwenChatModel.chat(userMessage);
        AiMessage aiMessage = chatResponse.aiMessage();
        log.info("ai 输出:" + aiMessage.toString());
        return aiMessage.text();
    }

    // 3. 添加了系统提示词的聊天
    public String chatWithSystemMessage(String message) {
        SystemMessage systemMessage = SystemMessage.from(SYSTEM_MESSAGE);
        UserMessage userMessage =  UserMessage.from( message);
        ChatResponse chatResponse = qwenChatModel.chat(systemMessage, userMessage);
        AiMessage aiMessage = chatResponse.aiMessage();
        log.info("ai 输出:" + aiMessage.toString());
        return aiMessage.text();
    }
}
