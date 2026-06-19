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

    @NotNull(message = "Flight id is required")
    private Long flightId;

    @NotNull(message = "Schedule id is required")
    private Long scheduleId;

    @NotNull(message = "Departure date time is required")
    private LocalDateTime departureDateTime;

    @NotNull(message = "Arrival date-time is required")
    private LocalDateTime arrivalDateTime;

    @NotNull(message = "Total seats is required")
    @Positive(message = "Total seats must be positive")
    private Integer totalSeats;

    @PositiveOrZero(message = "Available seats cannot be negative")
    private Integer availableSeats;

    private FlightStatus flightStatus;

    @Builder.Default
    private Boolean active;

    @PositiveOrZero(message = "Minimum advance booking days cannot be negative")
    private Integer minAdvanceBookingDays;

    @PositiveOrZero(message = "Minimum advance booking days cannot be negative")
    private Integer maxAdvanceBookingDays;

}
