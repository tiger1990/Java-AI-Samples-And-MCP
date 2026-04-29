package com.travel.travelmonk.prompt;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/acme")
public class TravelMonkController {

    public final ChatClient chatClient;

    public TravelMonkController(@Qualifier("anthropicChatClient") ChatClient chatClient){
        this.chatClient = chatClient;
    }

    @GetMapping("/monk/chat")
    public String chat(@RequestParam String message){
        var systemInstruction = """
                You are customer service assistant for TravelMonk.
                You can only discuss:
                - Travel plans, like start date of travel return date from travel
                - Types of Hotels, Home Stays, Leisure Services, Vehicle Options.
                If asked about anything else, respond: "I can help with only banking related questions."
                """;
        return chatClient.prompt()
                .user(message)
                .system(systemInstruction)
                .call()
                .content();
    }
}
