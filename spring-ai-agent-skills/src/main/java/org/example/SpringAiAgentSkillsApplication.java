package org.example;

import lombok.extern.slf4j.Slf4j;
import org.springaicommunity.agent.tools.ShellTools;
import org.springaicommunity.agent.tools.SkillsTool;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;

@Slf4j
@SpringBootApplication
public class SpringAiAgentSkillsApplication implements ApplicationRunner {

    public static void main(String[] args) {
        SpringApplication.run(SpringAiAgentSkillsApplication.class, args);
    }

    @Autowired
    ChatClient.Builder chatClientBuilder;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("测试skill");
        ChatClient chatClient = chatClientBuilder
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .defaultToolCallbacks(SkillsTool.builder()
                        // 注册我们上面的skill目录
                        .addSkillsResource(new ClassPathResource("agents/skills"))
                        .build())
                // import org.springaicommunity.agent.tools.FileSystemTools; 入参数有ToolContext，用不到还会被校验拦住，这里重写一个替换来使用
                .defaultTools(FileSystemTools.builder().build())
                .defaultTools(ShellTools.builder().build())
                .build();

        String content = chatClient.prompt()
                // 这里我们写死了读取指定的文件；最后将审查后的结果写入result.md文件中
                .user("审查以下类是否符合最佳实践: %s。最终文件写入: %s"
                        .formatted(
                                "/Users/kiy/IdeaProjects/spring-demo/spring-ai-agent-skills/src/main/java/org/example/SpringAiAgentSkillsApplication.java",
                                "result.md"))
                .call()
                .content();

        log.info(content);
    }

}