package org.example;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.document.DocumentReader;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/ai")
public class RagRedisController implements InitializingBean {

    @Autowired
    private VectorStore vectorStore;
    @Autowired
    private ChatModel chatModel;

    @Override
    public void afterPropertiesSet() throws Exception {
        // 1. parse document
        DocumentReader reader = new TextReader("classpath:rag.txt");
        List<Document> documents = reader.get();
        log.info("{} documents loaded", documents.size());

        // 2. split trunks
        List<Document> splitDocuments = new TokenTextSplitter().apply(documents);
        log.info("{} documents split", splitDocuments.size());

        // 3. create embedding and store to vector store
        log.info("create embedding and save to vector store");
        vectorStore.add(splitDocuments);
    }

    @GetMapping
    public Flux<String> chat(@RequestParam(value = "message",
            defaultValue = "考勤规定是什么?") String message) {
        return ChatClient.builder(chatModel).build().prompt()
                .advisors(new QuestionAnswerAdvisor(vectorStore))
                .user(message)
                .stream().chatResponse()
                .map(x -> x.getResult().getOutput().getText());
    }

}
