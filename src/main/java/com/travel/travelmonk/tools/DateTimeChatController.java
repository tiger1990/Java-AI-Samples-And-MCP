package com.travel.travelmonk.tools;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DateTimeChatController {

    public final ChatClient chatClient;

    public DateTimeChatController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @GetMapping("/tools")
    public String tools(){
        return chatClient.prompt("What day  is tomorrow?")
                .tools(new DateTimeTools()).call().content();
    }
}
