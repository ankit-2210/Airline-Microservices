package com.airlineportal.payload.request.Flight;

import com.airlineportal.utils.Flight.FlightStatus;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.*;
import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
    @Min(value = 1, message = "Total seats must be greater than zero")
    private Integer totalSeats;

    @NotNull(message = "Available seats is required")
    @Min(value = 0, message = "Available seats cannot be negative")
    private Integer availableSeats;

    private FlightStatus flightStatus;

    @Min(value = 0, message = "Minimum advance booking days cannot be negative")
    private Integer minAdvanceBookingDays;

    @Min(value = 0, message = "Maximum advance booking days cannot be negative")
    private Integer maxAdvanceBookingDays;

    @Builder.Default
    private Boolean active = true;

}
