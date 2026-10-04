package com.airlineportal.payload.response.CheckIn;

import com.airlineportal.utils.CheckIn.CheckInStatus;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckInResponse {
    private Long id;
    private Long bookingId;
    private Long passengerId;
    private Long flightInstanceId;

    private String pnr;
    private String seatNumber;

    private CheckInStatus status;

    private LocalDateTime checkedInAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


}
