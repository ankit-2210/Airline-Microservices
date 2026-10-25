package com.airlineportal.payload.request.Notification;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationSearchRequest {
    private Long userId;

    private String eventType;
    private String status;

    private Instant from;
    private Instant to;

    @Builder.Default
    private int page = 0;

    @Builder.Default
    private int size = 20;

    @Builder.Default
    private String sortBy = "createdAt";

    @Builder.Default
    private String sortDirection = "DESC";


}
