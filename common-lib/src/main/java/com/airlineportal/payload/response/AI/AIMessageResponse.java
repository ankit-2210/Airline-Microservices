package com.airlineportal.payload.response.AI;


import com.airlineportal.utils.AI.MessageRole;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AIMessageResponse {
    private Long id;

    private MessageRole role;
    private String content;

    private LocalDateTime timestamp;

}
