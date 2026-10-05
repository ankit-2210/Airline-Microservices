package com.checkinservice.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckInReportData {
    private Long id;
    private Long bookingId;
    private Long passengerId;
    private Long flightInstanceId;

    private String pnr;
    private String seatNumber;
    private String status;

    private LocalDateTime checkedInAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
