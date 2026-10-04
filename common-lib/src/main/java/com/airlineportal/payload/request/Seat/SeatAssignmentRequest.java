package com.airlineportal.payload.request.Seat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeatAssignmentRequest {
    @NotNull(message = "Flight instance id is required")
    private Long flightInstanceId;

    @NotBlank(message = "Seat number is required")
    private String seatNumber;

    @NotNull(message = "Booking id is required")
    private Long bookingId;


}
