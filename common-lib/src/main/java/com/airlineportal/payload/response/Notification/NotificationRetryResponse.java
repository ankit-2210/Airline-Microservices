package com.airlineportal.payload.response.Notification;


import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRetryResponse {
    private Long notificationId;

    private String status;
    private String message;

    private Instant nextAttemptAt;


}
