package com.microservices.payload.response.Flight;

import com.microservices.payload.response.Airport.AirportResponse;
import com.microservices.utils.Flight.FlightStatus;
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlightInstanceResponse {
    private Long id;

    private Long flightId;
    private String flightNumber;

    private Long scheduleId;

    private Long airlineId;
    private String airlineName;
    private String airlineLogo;

    private Long aircraftId;
    private String aircraftModel;
    private String aircraftCode;

    private AirportResponse departureAirport;
    private AirportResponse arrivalAirport;

    private LocalDateTime departureDateTime;
    private LocalDateTime arrivalDateTime;

    private String formattedDuration;

    private Integer totalSeats;
    private Integer availableSeats;
    private Integer bookedSeats;

    private FlightStatus flightStatus;
    private Boolean active;

    private Integer minAdvanceBookingDays;
    private Integer maxAdvanceBookingDays;

    private Boolean soldOut;
    private Boolean bookingOpen;
    private Boolean canCheckIn;

    private Instant createdAt;
    private Instant updatedAt;

}
