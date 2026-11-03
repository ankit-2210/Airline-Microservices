package com.promptservice.repository;


import com.airlineportal.utils.AI.MessageRole;
import com.promptservice.model.AIMessage;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface AIMessageRepository extends JpaRepository<AIMessage, Long> {
    Page<AIMessage> findByConversationIdOrderByCreatedAtAsc(Long conversationId, Pageable pageable);

    List<AIMessage> findByConversationIdOrderByCreatedAtAsc(Long conversationId);

    long countByConversationId(Long conversationId);
    long countByConversationIdAndRole(Long conversationId, MessageRole role);


}
