package com.promptservice.config;

import com.promptservice.memory.PostgresChatMemoryStore;
import com.promptservice.assistant.AirlineAssistant;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LangChain4jConfig {
    @Bean
    public AirlineAssistant airlineAssistant(ChatModel geminiChatModel, PostgresChatMemoryStore memoryStore) {

        return AiServices.builder(AirlineAssistant.class)
                .chatModel(geminiChatModel)
                .chatMemoryProvider(memoryId ->
                        MessageWindowChatMemory.builder()
                                .id(memoryId)
                                .maxMessages(20)
                                .chatMemoryStore(memoryStore)
                                .build()
                )
                .build();
    }




}
