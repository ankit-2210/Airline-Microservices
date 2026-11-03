package com.airlineportal.payload.request.AI;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AIChatRequest {

    private Long conversationId;

    @NotBlank(message = "Message cannot be empty")
    @Size(max = 5000, message = "Message cannot exceed 5000 characters")
    private String message;


}
