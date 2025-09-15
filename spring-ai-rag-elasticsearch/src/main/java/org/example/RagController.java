package org.example;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.mapping.*;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.elasticsearch.indices.IndexSettings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.document.DocumentReader;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.elasticsearch.autoconfigure.ElasticsearchVectorStoreProperties;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/ai")
public class RagController {

    private static final String textField = "content";

    private static final String vectorField = "embedding";

    private VectorStore vectorStore;
    private ChatClient chatClient;
    private ChatModel chatModel;
    private ElasticsearchClient elasticsearchClient;
    private ElasticsearchVectorStoreProperties options;

    @PostMapping("/rag/import")
    public void inport(MultipartFile file) throws Exception {
        // 1. parse document
        DocumentReader reader = new PagePdfDocumentReader(file.getResource());
        List<Document> documents = reader.get();
        log.info("{} documents loaded", documents.size());

        // 2. split trunks
        List<Document> splitDocuments = new TokenTextSplitter().apply(documents);
        log.info("{} documents split", splitDocuments.size());

        // 3. create embedding and store to vector store
        log.info("create embedding and save to vector store");
        createIndexIfNotExists();
        vectorStore.add(splitDocuments);
    }

    @GetMapping("/rag/chat")
    public Flux<String> chat(@RequestParam(value = "message",
            defaultValue = "how to get start with spring ai alibaba?") String message) {
        // Enable hybrid search, both embedding and full text search
        SearchRequest searchRequest = SearchRequest.builder().
                topK(4)
                .similarityThresholdAll()
                .filterExpression(new FilterExpressionBuilder().eq(textField, message).build())
                .build();

        // Step3 - Retrieve and llm generate
        String promptTemplate = """
                Context information is below.
                ---------------------
                {question_answer_context}
                ---------------------
                Given the context and provided history information and not prior knowledge,
                reply to the user comment. If the answer is not in the context, inform
                the user that you can't answer the question.
                """;

        QuestionAnswerAdvisor questionAnswerAdvisor = QuestionAnswerAdvisor.builder(vectorStore)
                .searchRequest(searchRequest)
                .promptTemplate(new SystemPromptTemplate(promptTemplate))
                .build();

        ChatClient chatClient = ChatClient.builder(chatModel)
                .defaultAdvisors(questionAnswerAdvisor)
                .build();

        return chatClient.prompt()
                .user(message)
                .stream().chatResponse()
                .map(x -> x.getResult().getOutput().getText());
    }

    private void createIndexIfNotExists() {
        try {
            String indexName = options.getIndexName();
            Integer dimsLength = options.getDimensions();

            if (StringUtils.hasText(indexName)) {
                throw new IllegalArgumentException("Elastic search index name must be provided");
            }

            boolean exists = elasticsearchClient.indices().exists(idx -> idx.index(indexName)).value();
            if (exists) {
                log.debug("Index {} already exists. Skipping creation.", indexName);
                return;
            }

            String similarityAlgo = options.getSimilarity().name();
            IndexSettings indexSettings = IndexSettings
                    .of(settings -> settings.numberOfShards(String.valueOf(1)).numberOfReplicas(String.valueOf(1)));

            // Maybe using json directly?
            Map<String, Property> properties = new HashMap<>();
            properties.put(vectorField, Property.of(property -> property.denseVector(
                    DenseVectorProperty.of(dense -> dense.index(true).dims(dimsLength).similarity(similarityAlgo)))));
            properties.put(textField, Property.of(property -> property.text(TextProperty.of(t -> t))));

            Map<String, Property> metadata = new HashMap<>();
            metadata.put("ref_doc_id", Property.of(property -> property.keyword(KeywordProperty.of(k -> k))));

            properties.put("metadata",
                    Property.of(property -> property.object(ObjectProperty.of(op -> op.properties(metadata)))));

            CreateIndexResponse indexResponse = elasticsearchClient.indices()
                    .create(createIndexBuilder -> createIndexBuilder.index(indexName)
                            .settings(indexSettings)
                            .mappings(TypeMapping.of(mappings -> mappings.properties(properties))));

            if (!indexResponse.acknowledged()) {
                throw new RuntimeException("failed to create index");
            }

            log.info("create elasticsearch index {} successfully", indexName);
        } catch (IOException e) {
            log.error("failed to create index", e);
            throw new RuntimeException(e);
        }
    }


}
