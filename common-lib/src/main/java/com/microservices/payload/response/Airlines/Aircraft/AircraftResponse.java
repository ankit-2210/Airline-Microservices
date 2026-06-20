package com.microservices.payload.response.Airlines.Aircraft;

import com.microservices.utils.Airline.AircraftStatus;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AircraftResponse {
    private Long id;

    private String code;
    private String model;
    private String manufacturer;

    private Integer seatingCapacity;
    private Integer economySeats;
    private Integer premiumEconomySeats;
    private Integer businessSeats;
    private Integer firstClassSeats;
    private Integer rangeKm;
    private Integer cruisingSpeedKmh;
    private Integer maxAltitudeFt;
    private Integer yearOfManufacture;

    private LocalDate registrationDate;
    private LocalDate nextMaintenanceDate;

    private AircraftStatus aircraftStatus;

    private Boolean isAvailable;

    private Long airlineId;
    private String airlineName;
    private String airlineIataCode;
    private String airlineLogo;

    private Long currentAirportId;
    private Long currentAirportCityId;
    private String currentAirportCode;
    private String currentAirportName;

    private Integer totalSeats;
    private Boolean requiresMaintenance;
    private Boolean isOperational;

    private Instant createdAt;
    private Instant updatedAt;

}
