package com.microservices.payload.response.Flight;

import lombok.*;

import java.time.*;
import java.util.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlightScheduleResponse {
    private Long id;

    private Long flightId;
    private String flightNumber;

    private Long departureAirportId;
    private String departmentAirportCode;

    private Long arrivalAirportId;
    private String arrivalAirportCode;

    private LocalTime departureTime;
    private LocalTime arrivalTime;

    private LocalDate startDate;
    private LocalDate endDate;

    private List<DayOfWeek> operatingDays;

    private Boolean active;

    private Instant createdAt;
    private Instant updatedAt;

}
