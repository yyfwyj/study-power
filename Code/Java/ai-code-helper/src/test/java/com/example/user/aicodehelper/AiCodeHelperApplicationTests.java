package com.example.user.aicodehelper;

import com.example.user.aicodehelper.ai.AiCodeHelper;
import dev.langchain4j.data.message.ImageContent;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.data.message.UserMessage;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest // 单元测试可以帮我们自动注入一些bean
class AiCodeHelperApplicationTests {

    @Resource
    private AiCodeHelper aiCodeHelper;

    // ai对话单元测试
    @Test
    void chat() {
        aiCodeHelper.chat("写一个java程序，求1+2+3+4+5+6+7+8+9+10");
    }

    @Test
    void chatWithMessage() {
        UserMessage userMessage = UserMessage.from(
                TextContent.from("描述图片"),
                ImageContent.from("https://picx.zhimg.com/v2-d6f44389971daab7e688e5b37046e4e4_720w.jpg?source=172ae18b")
        );
        aiCodeHelper.chatWithMessage(userMessage);
    }

    @Test
    void chatWithSystemMessage() {
        aiCodeHelper.chatWithSystemMessage("我想学AI，将AI集成到现有项目并且前端展示，应该学习什么知识");
    }
}
