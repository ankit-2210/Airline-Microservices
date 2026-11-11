package com.promptservice.service;


import com.airlineportal.payload.request.AI.AIChatRequest;
import com.airlineportal.payload.response.AI.AIChatResponse;
import com.airlineportal.utils.AI.ConversationStatus;
import com.promptservice.assistant.AirlineAssistant;
import com.promptservice.model.AIConversation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.*;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AIChatService {
    private final AirlineAssistant airlineAssistant;
    private final AIContextService aiContextService;
    private final AIConversationService aiConversationService;

    @Transactional
    public AIChatResponse chat(AIChatRequest request) {
        Long userId = aiContextService.getCurrentUserId();

        AIConversation conversation;
        if (request.getConversationId() == null) {
            conversation = aiConversationService.createConversation(userId, null);
        }
        else {
            conversation = aiConversationService.getConversation(request.getConversationId(), userId);

            if (conversation.getStatus() != ConversationStatus.ACTIVE) {
                throw new IllegalStateException("This conversation is no longer active");
            }
        }

        String response = airlineAssistant.chat(conversation.getId().toString(), request.getMessage());

        return AIChatResponse.builder()
                .conversationId(conversation.getId())
                .response(response)
                .timestamp(LocalDateTime.now())
                .success(true)
                .build();
    }


}
