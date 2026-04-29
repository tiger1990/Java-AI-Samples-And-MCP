package com.travel.travelmonk.weather;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/weather")
public class WeatherController {

    public final ChatClient chatClient;
    private final WeatherTools tools;

    public WeatherController(ChatClient.Builder builder, WeatherTools tools) {
        this.chatClient = builder.build();
        this.tools = tools;
    }

    @GetMapping("/alerts")
    public String getAlerts(@RequestParam String message) {
        return chatClient.prompt()
                .tools(tools)
                .user(message)
                .call()
                .content();
    }
}