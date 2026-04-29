package com.travel.travelmonk.vacation;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VacationPlan {

    public final ChatClient chatClient;

    public VacationPlan(@Qualifier("openAiChatClient") ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/vacations/unstructured")
    public String unstructured(){
        return chatClient.prompt()
                .user("I want to plan a trip to Rishikesh, Give me a list of things to do")
                .call()
                .content();
    }

    @GetMapping("/vacations/structured")
    public Itinerary structured(){
        return chatClient.prompt()
                .user("I want to plan a trip to Rishikesh, Give me a list of things to do")
                .call()
                .entity(Itinerary.class);
    }

}
