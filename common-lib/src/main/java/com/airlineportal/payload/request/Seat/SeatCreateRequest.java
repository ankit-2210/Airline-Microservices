package com.airlineportal.payload.request.Seat;

import com.airlineportal.utils.Seat.SeatClass;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeatCreateRequest {
    @NotNull(message = "Flight instance id is required")
    private Long flightInstanceId;

    @NotBlank(message = "Seat number is required")
    private String seatNumber;

    @NotNull(message = "Seat class is required")
    private SeatClass seatClass;


}
