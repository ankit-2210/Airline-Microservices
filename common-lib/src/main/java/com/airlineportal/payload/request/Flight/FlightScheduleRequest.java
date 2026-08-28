package com.airlineportal.payload.request.Flight;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.*;
import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FlightScheduleRequest {
    @NotNull(message = "Flight id is required")
    private Long flightId;

    @NotNull(message = "Departure time is required")
    private LocalTime departureTime;

    @NotNull(message = "Arrival time is required")
    private LocalTime arrivalTime;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;

    @NotEmpty(message = "At least one operating day is required")
    @Builder.Default
    private Set<DayOfWeek> operatingDays = new HashSet<>();

    @Builder.Default
    private Boolean active=true;

}
