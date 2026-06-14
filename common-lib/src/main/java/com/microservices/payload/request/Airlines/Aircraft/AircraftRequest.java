package com.microservices.payload.request.Airlines.Aircraft;

import com.microservices.utils.Airline.AircraftStatus;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AircraftRequest {
    @NotBlank(message = "Aircraft code is required")
    private String code;

    @NotBlank(message = "Aircraft model is required")
    private String model;

    @NotBlank(message = "Manufacturer is required")
    private String manufacturer;

    @NotNull(message = "Seating capacity is required")
    @Positive(message = "Seating capacity must be positive")
    private Integer seatingCapacity;

    @PositiveOrZero
    private Integer economySeats;

    @PositiveOrZero
    private Integer premiumEconomySeats;

    @PositiveOrZero
    private Integer businessSeats;

    @PositiveOrZero
    private Integer firstClassSeats;

    @Positive(message = "Range must be positive")
    private Integer rangeKm;

    @Positive(message = "Cruising speed must be positive")
    private Integer cruisingSpeedKmh;

    @Positive(message = "Maximum altitude must be positive")
    private Integer maxAltitudeFt;

    @Positive(message = "Year of manufacture must be positive")
    private Integer yearOfManufacture;

    private LocalDate registrationDate;

    private LocalDate nextMaintenanceDate;

    @NotNull(message = "Status is required")
    private AircraftStatus aircraftStatus;

    @NotNull(message = "Availability status is required")
    private Boolean isAvailable;

    private Long currentAirportId;

}
