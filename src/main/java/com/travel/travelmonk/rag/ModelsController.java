package com.travel.travelmonk.rag;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ModelsController {
    public final ChatClient chatClient;

    public ModelsController(ChatClient.Builder builder, VectorStore vectorStore) {
        this.chatClient = builder.defaultAdvisors(QuestionAnswerAdvisor.builder(vectorStore)
                .searchRequest(SearchRequest.builder()
                        .build()).build()).build();
        //.similarityThreshold(0.8d).topK(6)
    }

    @GetMapping("/rag/models")
    public Models faq(
            @RequestParam(value = "message", defaultValue = "Give me the list of all openAi along with their context window size")
            String message) {

        return chatClient.prompt().user(message).call().entity(Models.class);

    }
}
