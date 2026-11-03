package com.promptservice.config;


import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Getter
@Setter
@Configuration
public class GeminiConfig {
    @Value("${gemini.api.url}")
    private String url;

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.model:gemini-3.8-flash}")
    private String model;

    @Bean
    public ChatModel geminiChatModel() {

        return GoogleAiGeminiChatModel.builder()
                .apiKey(apiKey)
                .modelName(model)
                .temperature(0.2)
                .maxOutputTokens(2048)
                .timeout(Duration.ofSeconds(60))
                .maxRetries(2)
                .build();
    }

}
