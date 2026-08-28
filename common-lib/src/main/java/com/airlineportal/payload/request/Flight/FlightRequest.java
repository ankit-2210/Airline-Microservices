package com.airlineportal.payload.request.Flight;

import com.airlineportal.utils.Flight.FlightStatus;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FlightRequest {

    @NotBlank(message = "Flight number is required")
    @Size(max = 20, message = "Flight number cannot exceed 20 characters")
    private String flightNumber;

    @NotNull(message = "Airline id is required")
    private Long airlineId;

    @NotNull(message = "Aircraft id is required")
    private Long aircraftId;

    @NotNull(message = "Departure airport id is required")
    private Long departureAirportId;

    @NotNull(message = "Arrival airport id is required")
    private Long arrivalAirportId;

    @NotNull(message = "Scheduled departure is required")
    private LocalDateTime scheduledDeparture;

    @NotNull(message = "Scheduled arrival is required")
    private LocalDateTime scheduledArrival;

    private LocalDateTime actualDeparture;

    private LocalDateTime actualArrival;

    private FlightStatus flightStatus;

    @Builder.Default
    private Boolean active = true;

}
