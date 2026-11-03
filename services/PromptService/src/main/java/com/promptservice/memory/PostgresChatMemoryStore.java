package com.promptservice.memory;


import com.airlineportal.utils.AI.MessageRole;
import com.promptservice.model.AIConversation;
import com.promptservice.model.AIMessage;
import com.promptservice.repository.AIConversationRepository;
import com.promptservice.repository.AIMessageRepository;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component
@RequiredArgsConstructor
public class PostgresChatMemoryStore implements ChatMemoryStore {
    private final AIConversationRepository conversationRepository;
    private final AIMessageRepository messageRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ChatMessage> getMessages(Object memoryId) {
        Long conversationId = Long.valueOf(memoryId.toString());
        if (!conversationRepository.existsById(conversationId)) {
            return List.of();
        }

        return messageRepository.findByConversationIdOrderByCreatedAtAsc(conversationId)
                .stream()
                .map(this::toChatMessage)
                .toList();
    }

    @Override
    @Transactional
    public void updateMessages(Object memoryId, List<ChatMessage> messages) {
        Long conversationId = Long.valueOf(memoryId.toString());
        AIConversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new IllegalArgumentException("Conversation not found"));

        messageRepository.deleteAll(messageRepository.findByConversationIdOrderByCreatedAtAsc(conversationId));

        List<AIMessage> entities = messages.stream()
                .map(message -> toEntity(conversation, message))
                .toList();

        messageRepository.saveAll(entities);
    }

    @Override
    @Transactional
    public void deleteMessages(Object memoryId) {
        Long conversationId = Long.valueOf(memoryId.toString());
        messageRepository.deleteAll(messageRepository.findByConversationIdOrderByCreatedAtAsc(conversationId));
    }




    private ChatMessage toChatMessage(AIMessage message) {

        return switch (message.getRole()) {
            case USER -> UserMessage.from(message.getContent());
            case ASSISTANT -> AiMessage.from(message.getContent());
            case SYSTEM -> SystemMessage.from(message.getContent());
        };
    }

    private AIMessage toEntity(AIConversation conversation, ChatMessage message) {
        MessageRole role;
        String content;

        if (message instanceof UserMessage userMessage) {
            role = MessageRole.USER;
            content = userMessage.singleText();
        }
        else if (message instanceof AiMessage aiMessage) {
            role = MessageRole.ASSISTANT;
            content = aiMessage.text();
        }
        else if (message instanceof SystemMessage systemMessage) {
            role = MessageRole.SYSTEM;
            content = systemMessage.text();
        }
        else {
            throw new IllegalArgumentException("Unsupported chat message type: " + message.getClass().getSimpleName());
        }

        return AIMessage.builder()
                .conversation(conversation)
                .role(role)
                .content(content)
                .build();
    }

}
