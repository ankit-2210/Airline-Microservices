package com.microservices.payload.request.Flight;

import com.microservices.utils.Flight.FlightStatus;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlightRequest {
    @NotBlank(message = "Flight number is required")
    private String flightNumber;

    @NotNull(message = "Airline is required")
    private Long airlineId;

    @NotNull(message = "Aircraft is required")
    private Long aircraftId;

    @NotNull(message = "Departure airport is required")
    private Long departureAirportId;

    @NotNull(message = "Arrival airport is required")
    private Long arrivalAirportId;

    @NotNull(message = "Scheduled departure is required")
    private LocalDateTime scheduledDeparture;

    @NotNull(message = "Scheduled arrival is required")
    private LocalDateTime scheduledArrival;

    private LocalDateTime actualDeparture;

    private LocalDateTime actualArrival;

    @NotNull(message = "Flight status is required")
    private FlightStatus flightStatus;

    private Boolean active;

}
