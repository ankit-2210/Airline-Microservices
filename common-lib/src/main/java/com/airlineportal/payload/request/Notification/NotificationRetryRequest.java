package com.airlineportal.payload.request.Notification;

import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor


public class NotificationRetryRequest {
    @Size(max = 500, message = "Retry reason must not exceed 500 characters")
    private String reason;


}
