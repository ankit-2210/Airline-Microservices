package com.microservices.payload.request.Flight;


import com.microservices.utils.Flight.FlightStatus;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlightInstanceRequest {

    @NotNull(message = "Flight Id is required")
    private Long flightId;

    private Long scheduleId;

    @NotNull(message = "Departure date-time is required")
    private LocalDateTime departureDateTime;

    @NotNull(message = "Arrival date-time is required")
    private LocalDateTime arrivalDateTime;

    @NotNull(message = "Total seats is required")
    @Positive(message = "Total seats must be greater than zero")
    private Integer totalSeats;

    @PositiveOrZero(message = "Available seats cannot be negative")
    private Integer availableSeats;

    private FlightStatus flightStatus;

    private Boolean active;

    @PositiveOrZero
    private Integer minAdvanceBookingDays;

    @PositiveOrZero
    private Integer maxAdvanceBookingDays;


}
