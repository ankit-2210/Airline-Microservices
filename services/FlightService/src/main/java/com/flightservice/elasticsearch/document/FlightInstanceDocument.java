package com.flightservice.elasticsearch.document;

import com.airlineportal.utils.Flight.FlightStatus;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "flight_instances")
public class FlightInstanceDocument {
    @Id
    private Long id;

    private Long flightId;
    private String flightNumber;

    private Long airlineId;
    private String airlineName;
    private String airlineLogo;

    private Long aircraftId;
    private String aircraftModel;
    private String aircraftCode;

    private Long departureAirportId;
    private String departureAirportCode;
    private String departureAirportName;

    private Long arrivalAirportId;
    private String arrivalAirportCode;
    private String arrivalAirportName;

    private LocalDateTime departureDateTime;
    private LocalDateTime arrivalDateTime;

    private Integer totalSeats;
    private Integer availableSeats;
    private Integer bookedSeats;

    private FlightStatus flightStatus;

    private Integer minAdvanceBookingDays;
    private Integer maxAdvanceBookingDays;

    private Boolean active;



}
