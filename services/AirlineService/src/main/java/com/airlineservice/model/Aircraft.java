package com.airlineservice.model;


import com.microservices.utils.Airline.AircraftStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(
        name = "aircraft",
        indexes = {
                @Index(name = "idx_aircraft_code", columnList = "code"),
                @Index(name = "idx_aircraft_airline", columnList = "airline_id"),
                @Index(name = "idx_aircraft_status", columnList = "aircraft_status")
        }
)
@EntityListeners(AuditingEntityListener.class)
public class Aircraft {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 30)
    private String code;

    @Column(nullable = false)
    private String model;

    @Column(nullable = false, length=50)
    private String manufacturer;

    @Column(nullable = false)
    private Integer seatingCapacity;

    @Column(name = "economy_seats")
    private Integer economySeats = 0;

    @Column(name = "premium_economy_seats")
    private Integer premiumEconomySeats = 0;

    @Column(name = "business_seats")
    private Integer businessSeats = 0;

    @Column(name = "first_class_seats")
    private Integer firstClassSeats = 0;

    @Column(name = "range_km")
    private Integer rangeKm;

    @Column(name = "cruising_speed_kmh")
    private Integer cruisingSpeedKmh;

    @Column(name = "max_altitude_ft")
    private Integer maxAltitudeFt;

    @Column(name = "year_of_manufacture")
    private Integer yearOfManufacture;

    private LocalDate registrationDate;

    private LocalDate nextMaintenanceDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "aircraft_status", nullable = false, length = 20)
    private AircraftStatus aircraftStatus = AircraftStatus.ACTIVE;

    @Builder.Default
    private Boolean isAvailable = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "airline_id", nullable = false)
    @ToString.Exclude
    private Airline airline;

    private Long currentAirportId;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private Instant updatedAt;


    @Transient
    public Integer getTotalSeats() {
        return (economySeats == null ? 0 : economySeats) + (premiumEconomySeats == null ? 0 : premiumEconomySeats)
                + (businessSeats == null ? 0 : businessSeats) + (firstClassSeats == null ? 0 : firstClassSeats);
    }

    @Transient
    public Boolean getRequiresMaintenance() {
        return nextMaintenanceDate != null && nextMaintenanceDate.isBefore(LocalDate.now().plusWeeks(2));
    }

    @Transient
    public Boolean getOperational() {
        return AircraftStatus.ACTIVE.equals(aircraftStatus) && Boolean.TRUE.equals(isAvailable);
    }


}
