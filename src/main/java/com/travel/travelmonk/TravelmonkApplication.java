package com.travel.travelmonk;

import com.travel.travelmonk.mcp.Session;
import com.travel.travelmonk.mcp.SessionTools;
import org.springframework.ai.anthropic.AnthropicChatModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class TravelmonkApplication {

	public static void main(String[] args) {
		SpringApplication.run(TravelmonkApplication.class, args);
	}

	@Bean
	public ChatClient openAiChatClient(OpenAiChatModel chatModel){
		return ChatClient.create(chatModel);
	}

	@Bean
	public ChatClient anthropicChatClient(AnthropicChatModel chatModel){
		return ChatClient.create(chatModel);
	}

	// Make tools available
	@Bean
	public List<ToolCallback> springIoSessionTools(SessionTools sessionTools) {
		return List.of(ToolCallbacks.from(sessionTools));
	}
}
