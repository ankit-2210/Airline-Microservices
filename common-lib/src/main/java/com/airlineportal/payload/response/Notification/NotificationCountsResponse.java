package com.airlineportal.payload.response.Notification;


import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationCountsResponse {
    private long pending;
    private long processing;
    private long sent;
    private long failed;

}
