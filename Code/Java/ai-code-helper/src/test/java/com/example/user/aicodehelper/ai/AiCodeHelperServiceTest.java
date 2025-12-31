package com.example.user.aicodehelper.ai;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AiCodeHelperServiceTest {

    @Autowired
    private AiCodeHelperService aiCodeHelperService;

    @Test
    void chat() {
        String result = aiCodeHelperService.chat("请给我一个java的hello world程序");
        System.out.println(result);
    }

    @Test
    void chatWithMemory() {
        String result = aiCodeHelperService.chat("请给我一个java的hello world程序");
        System.out.println(result);

       result =  aiCodeHelperService.chat("我上一次让你干什么来着");
       System.out.println(result);
    }

    @Test
    void chatForReport() {
        String userMessage = "你好，我是一名全栈开发，技术栈是前端+Java，请帮我制定一份学习AI应用开发的技术路线报告";
        AiCodeHelperService.Report report = aiCodeHelperService.chatForReport(userMessage);
            System.out.println(report);
    }

    @Test
    void chatWithTools() {
        String result = aiCodeHelperService.chat("有哪些常见的计算机网络面试题");
        System.out.println(result);
    }

    @Test
    void chatWithMcp() {
        String result = aiCodeHelperService.chat("什么是Figma？");
        System.out.println(result);
    }

    @Test
    void chatWithGuardrail() {
        String result = aiCodeHelperService.chat("kill the game");
        System.out.println(result);
    }
}