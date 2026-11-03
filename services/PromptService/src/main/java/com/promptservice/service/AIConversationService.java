package com.promptservice.service;


import com.airlineportal.payload.response.AI.AIConversationResponse;
import com.airlineportal.payload.response.AI.AIMessageResponse;
import com.airlineportal.utils.AI.ConversationStatus;
import com.airlineportal.utils.AI.MessageRole;
import com.promptservice.mapper.AIConversationMapper;
import com.promptservice.model.AIConversation;
import com.promptservice.model.AIMessage;
import com.promptservice.repository.AIConversationRepository;
import com.promptservice.repository.AIMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class AIConversationService {
    private final AIConversationRepository conversationRepository;
    private final AIMessageRepository messageRepository;

    // Create a new conversation
    public AIConversation createConversation(Long userId, String title) {
        AIConversation conversation = AIConversation.builder()
                .userId(userId)
                .title(title)
                .status(ConversationStatus.ACTIVE)
                .build();

        return conversationRepository.save(conversation);
    }

    // Retrieve conversation belonging to the authenticated user
    @Transactional(readOnly = true)
    public AIConversation getConversation(Long conversationId, Long userId) {
        return conversationRepository.findByIdAndUserId(conversationId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Conversation not found"));
    }

    // List conversations
    @Transactional(readOnly = true)
    public Page<AIConversationResponse> getConversations(Long userId, Pageable pageable) {
        return conversationRepository.findByUserIdAndStatus(userId, ConversationStatus.ACTIVE, pageable)
                .map(this::mapConversation);
    }

    // Get messages belonging to a conversation
    @Transactional(readOnly = true)
    public List<AIMessageResponse> getMessages(Long conversationId, Long userId) {
        AIConversation conversation = getConversation(conversationId, userId);
        return messageRepository.findByConversationIdOrderByCreatedAtAsc(conversation.getId())
                .stream()
                .map(AIConversationMapper::mapMessage)
                .toList();
    }

    // Save a message
    public AIMessage saveMessage(Long conversationId, Long userId, MessageRole role, String content) {
        AIConversation conversation = getConversation(conversationId, userId);

        AIMessage message = AIMessage.builder()
                .conversation(conversation)
                .role(role)
                .content(content)
                .build();

        if (conversation.getTitle() == null && role == MessageRole.USER) {
            conversation.setTitle(AIConversationMapper.generateTitle(content));
        }

        return messageRepository.save(message);
    }

    // Archive conversation
    public void archiveConversation(Long conversationId, Long userId) {
        AIConversation conversation = getConversation(conversationId, userId);

        conversation.setStatus(ConversationStatus.ARCHIVED);
        conversationRepository.save(conversation);
    }

    // Delete conversation and its messages
    public void deleteConversation(Long conversationId, Long userId) {
        AIConversation conversation = getConversation(conversationId, userId);
        conversationRepository.delete(conversation);
    }




    // Conversation response mapping
    private AIConversationResponse mapConversation(AIConversation conversation) {
        long messageCount = messageRepository.countByConversationId(conversation.getId());

        return AIConversationResponse.builder()
                .id(conversation.getId())

                .title(conversation.getTitle())
                .status(conversation.getStatus())

                .createdAt(conversation.getCreatedAt())
                .updatedAt(conversation.getUpdatedAt())

                .messageCount(messageCount)
                .build();
    }



}
