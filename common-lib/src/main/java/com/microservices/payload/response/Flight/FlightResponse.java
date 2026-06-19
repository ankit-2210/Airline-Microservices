package com.microservices.payload.response.Flight;

import com.microservices.payload.response.Airline.AircraftResponse;
import com.microservices.payload.response.Airline.AirlineResponse;
import com.microservices.payload.response.Airport.AirportResponse;
import com.microservices.utils.Flight.FlightStatus;
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

    private Double lowestPrice;
    private Integer totalAvailableSeats;

    private Instant createdAt;
    private Instant updatedAt;
}
