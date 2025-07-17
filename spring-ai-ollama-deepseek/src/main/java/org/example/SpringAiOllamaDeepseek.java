package org.example;

import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringAiOllamaDeepseek implements ApplicationRunner {

    public static void main(String[] args) {
        SpringApplication.run(SpringAiOllamaDeepseek.class, args);
    }

    @Autowired
    private OllamaChatModel ollamaChatModel;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String prompt = """
                你是一个人工智能，回答我提出的问题。
                """;
        ollamaChatModel.call(prompt + "问题是：今天几号");
    }

}
