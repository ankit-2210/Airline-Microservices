package com.airlineportal.payload.request.CheckIn;


import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckInRequest {
    @NotNull(message = "Booking id is required")
    private Long bookingId;

    @NotNull(message = "Passenger id is required")
    private Long passengerId;

    @NotNull(message = "User id is required")
    private Long userId;



}
