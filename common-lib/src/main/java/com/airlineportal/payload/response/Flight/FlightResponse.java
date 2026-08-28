package com.airlineportal.payload.response.Flight;


import com.airlineportal.payload.response.Airlines.Aircraft.AircraftResponse;
import com.airlineportal.payload.response.Airlines.Airline.AirlineResponse;
import com.airlineportal.payload.response.Location.Airport.AirportResponse;
import com.airlineportal.utils.Flight.FlightStatus;
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlightResponse {
    private Long id;
    private String flightNumber;

    private AirlineResponse airline;

    private AircraftResponse aircraft;

    private AirportResponse departureAirport;
    private AirportResponse arrivalAirport;

    private LocalDateTime scheduledDeparture;
    private LocalDateTime scheduledArrival;

    private LocalDateTime actualDeparture;
    private LocalDateTime actualArrival;

    private FlightStatus flightStatus;

    private Boolean active;
    private Boolean delayed;

    private Long delayMinutes;

    private Double lowestPrice;
    private Integer totalAvailableSeats;

    private Instant createdAt;
    private Instant updatedAt;
}
