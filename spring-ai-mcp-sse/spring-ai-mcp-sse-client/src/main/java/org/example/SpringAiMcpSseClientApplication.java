package org.example;

import io.modelcontextprotocol.client.McpSyncClient;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class SpringAiMcpSseClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringAiMcpSseClientApplication.class, args);
    }

    @Bean
    public CommandLineRunner chatbot(ChatClient.Builder chatClientBuilder, List<McpSyncClient> mcpSyncClients) {
        return args -> {
            SyncMcpToolCallbackProvider toolCallbackProvider = SyncMcpToolCallbackProvider.builder()
                    .mcpClients(mcpSyncClients)
                    .build();

            // 构建聊天客户端
            ChatClient chatClient = chatClientBuilder
                    //设置系统提示，引导 AI 的行为和角色
                    .defaultSystem("你是一个可以查询天气的助手，可以调用工具回答用户关于天气相关问题。")
                    .build();

            System.out.println("\n助手: " +
                    chatClient.prompt("北京今天天气怎么样？")
                            .toolCallbacks(toolCallbackProvider)
                            .call()
                            .content());
        };
    }


}