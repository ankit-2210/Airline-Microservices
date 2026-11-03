package com.promptservice.repository;


import com.airlineportal.utils.AI.ConversationStatus;
import com.promptservice.model.AIConversation;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface AIConversationRepository extends JpaRepository<AIConversation, Long> {

    Page<AIConversation> findByUserId(Long userId, Pageable pageable);
    Page<AIConversation> findByUserIdAndStatus(Long userId, ConversationStatus status, Pageable pageable);

    Optional<AIConversation> findByIdAndUserId(Long id, Long userId);

    boolean existsByIdAndUserId(Long id, Long userId);


}
