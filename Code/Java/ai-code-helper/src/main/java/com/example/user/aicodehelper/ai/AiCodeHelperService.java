package com.example.user.aicodehelper.ai;


import dev.langchain4j.service.SystemMessage;

import java.util.List;

public interface AiCodeHelperService {
    //  这个注解支持直接指定一个本地文件，文件里面就可以写入相关提示词
    @SystemMessage(fromResource = "System-prompt.txt")
    String chat(String userMessage);

    @SystemMessage(fromResource = "System-prompt.txt")
    Report chatForReport(String userMessage);

    // 学习报告
    record Report(String name, List<String> suggestionList){};
}
