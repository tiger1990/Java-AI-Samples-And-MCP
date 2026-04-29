package com.travel.travelmonk.prompt;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ArticleController {

    public final ChatClient chatClient;

    public ArticleController(@Qualifier("openAiChatClient") ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/posts/new")
    public String newPost(@RequestParam(
            value = "topic", defaultValue = "JDK virtual threads")
                          String topic) {
        var system = """
                Blog post generator guidelines:
                Generate post around length of 500 word which engage general audience.
                
                Structure:
                Establish topic relevance and Hook readers
                Develop 3 main points with supporting evidences and examples
                Summarize key takeaways and call to action 
                
                Content Requirements:
                Include real-world application or case studies 
                Incorporate relevant statistics or data points when appropriate
                Explain benefits/implications clearly for non experts
                
                Tone and Style:
                Write in an informative yet conversational voice
                Use accessible language while maintaining authority
                Break up text with subheading and short paragraphs
                
                Response Format: Deliver complete, ready to publish posts with a suggested title. 
                """;
        return chatClient.prompt()
                .user(u -> {
                            u.text("Write me a blog post  about {topic}");
                            u.param("topic", topic);
                        }
                ).system(system).call().content();

    }
}
