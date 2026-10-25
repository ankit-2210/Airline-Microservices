package com.notificationservice.message;

import java.util.*;

import lombok.*;

import java.util.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationMessage {

    private String eventKey;
    private String eventType;
    private Long userId;
    private String subject;
    private String templateName;

    @Builder.Default
    private Map<String, Object> variables = new HashMap<>();



}