package com.airlineportal.payload.response.Seat;

import com.airlineportal.utils.Seat.SeatClass;
import com.airlineportal.utils.Seat.SeatStatus;
import lombok.*;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeatResponse {
    private Long id;
    private Long flightInstanceId;

    private String seatNumber;

    private SeatClass seatClass;
    private SeatStatus seatStatus;

    private Long bookingId;

    private Instant createdAt;
    private Instant updatedAt;


}
