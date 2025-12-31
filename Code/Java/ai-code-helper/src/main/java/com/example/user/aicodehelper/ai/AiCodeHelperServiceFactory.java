package com.example.user.aicodehelper.ai;

import com.example.user.aicodehelper.ai.Tool.InterviewQuestionTool;
import dev.langchain4j.mcp.McpToolProvider;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// 配置类注解
@Configuration
public class AiCodeHelperServiceFactory {

    @Resource
    private ChatModel myQwenChatModel;

    @Resource
    private ContentRetriever contentRetriever;

    @Resource
    private McpToolProvider mcpToolProvider;

    @Resource
    private StreamingChatModel qwenStreamingChatModel;

    @Bean
    public AiCodeHelperService aiCodeHelperService() {
        // 设置对话记忆，并且实现最多保存N条记录，其余的会被淘汰（每个用户保存N条消息，它是支持区分用户的）
        ChatMemory chatMemory = MessageWindowChatMemory.withMaxMessages(10);
//        return AiServices.create(AiCodeHelperService.class, qwenchatModel);
        // 构造AIService
        return AiServices.builder(AiCodeHelperService.class)
                .chatModel(myQwenChatModel)
                .streamingChatModel(qwenStreamingChatModel) // 流式输出
                .chatMemoryProvider(memoryId -> MessageWindowChatMemory.withMaxMessages(10)) // 每个会话独立存储
                .chatMemory(chatMemory) // 会话记忆
                .contentRetriever(contentRetriever) // Rag 检索增强生成
                .tools(new InterviewQuestionTool()) // 工具调用
                .toolProvider(mcpToolProvider) // MCP 工具调用
                .build();
    }
}
