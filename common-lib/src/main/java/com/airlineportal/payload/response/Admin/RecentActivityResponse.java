package com.airlineportal.payload.response.Admin;

import lombok.*;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecentActivityResponse {
    private String activityType;
    private String description;
    private Long resourceId;
    private Instant timestamp;

}
