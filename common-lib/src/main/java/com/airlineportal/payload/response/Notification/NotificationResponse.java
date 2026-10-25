package com.airlineportal.payload.response.Notification;

import com.airlineportal.utils.Notification.NotificationChannel;
import com.airlineportal.utils.Notification.NotificationStatus;

import java.time.Instant;

public class NotificationResponse {
    private Long id;

    private String eventKey;
    private String eventType;

    private Long userId;

    private String recipientEmail;
    private String subject;
    private String templateName;

    private NotificationChannel channel;
    private NotificationStatus status;

    private Integer attemptCount;

    private Instant nextAttemptAt;
    private Instant sentAt;

    private String lastError;

    private Instant createdAt;
    private Instant updatedAt;


}
