package com.seatservice.dto;

import java.time.Instant;
import java.time.LocalDateTime;

import lombok.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeatReportData {
    private Long id;
    private Long flightInstanceId;

    private String seatNumber;
    private String seatClass;
    private String seatStatus;

    private Long bookingId;

    private String createdAt;
    private String updatedAt;

}
