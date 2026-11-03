package com.promptservice.mapper;

import com.airlineportal.payload.response.AI.AIMessageResponse;
import com.promptservice.model.AIMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class AIConversationMapper {

    // Message response mapping
    public static AIMessageResponse mapMessage(AIMessage message) {
        return AIMessageResponse.builder()
                .id(message.getId())

                .role(message.getRole())
                .content(message.getContent())

                .timestamp(message.getCreatedAt())
                .build();
    }

    public static String generateTitle(String message) {
        String normalized = message.trim().replaceAll("\\s+", " ");
        if (normalized.length() <= 60) {
            return normalized;
        }

        return normalized.substring(0, 57) + "...";
    }


}
