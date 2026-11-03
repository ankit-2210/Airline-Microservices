package com.airlineportal.payload.response.AI;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AIChatResponse {
    private Long conversationId;

    private String response;

    private LocalDateTime timestamp;

    private boolean success;


}
