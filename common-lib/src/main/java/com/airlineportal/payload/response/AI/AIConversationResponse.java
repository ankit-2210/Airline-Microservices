package com.airlineportal.payload.response.AI;

import com.airlineportal.utils.AI.ConversationStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AIConversationResponse {
    private Long id;

    private String title;
    private ConversationStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Long messageCount;



}
