package com.promptservice.model;


import com.airlineportal.utils.AI.ConversationStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "ai_conversations",
        indexes = {
                @Index(name = "idx_ai_conversation_user", columnList = "user_id"),
                @Index(name = "idx_ai_conversation_status", columnList = "status")
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AIConversation {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "user_id", nullable = false)
        private Long userId;

        @Column(length = 200)
        private String title;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false, length = 20)
        @Builder.Default
        private ConversationStatus status = ConversationStatus.ACTIVE;

        @Column(nullable = false, updatable = false)
        private LocalDateTime createdAt;

        @Column(nullable = false)
        private LocalDateTime updatedAt;

        @OneToMany(
                mappedBy = "conversation",
                cascade = CascadeType.ALL,
                orphanRemoval = true
        )
        @Builder.Default
        private List<AIMessage> messages = new ArrayList<>();

        @PrePersist
        public void onCreate() {
                LocalDateTime now = LocalDateTime.now();
                createdAt = now;
                updatedAt = now;

                if (status == null) {
                        status = ConversationStatus.ACTIVE;
                }
        }

        @PreUpdate
        public void onUpdate() {
                updatedAt = LocalDateTime.now();
        }


}
