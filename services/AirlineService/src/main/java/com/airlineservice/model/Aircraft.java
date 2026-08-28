package com.airlineservice.model;


import com.airlineportal.utils.Airline.AircraftStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
                @Index(name = "idx_aircraft_status", columnList = "aircraft_status"),
                @Index(name = "idx_aircraft_airport", columnList = "current_airport_id"),
                @Index(name = "idx_aircraft_available", columnList = "is_available"),
                @Index(name = "idx_aircraft_maintenance", columnList = "next_maintenance_date")
        }
)
@EntityListeners(AuditingEntityListener.class)
public class Aircraft {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Aircraft Identification
    @NotBlank
    @Column(unique = true, nullable = false, length = 30)
    private String code;

    @NotBlank
    @Column(nullable = false, length = 100)
    private String model;

    @NotBlank
    @Column(nullable = false, length = 50)
    private String manufacturer;

    // Seating
    @NotNull
    @Min(1)
    @Column(nullable = false)
    private Integer seatingCapacity;

    @Builder.Default
    @Min(0)
    @Column(name = "economy_seats", nullable = false)
    private Integer economySeats = 0;

    @Builder.Default
    @Min(0)
    @Column(name = "premium_economy_seats", nullable = false)
    private Integer premiumEconomySeats = 0;

    @Builder.Default
    @Min(0)
    @Column(name = "business_seats", nullable = false)
    private Integer businessSeats = 0;

    @Builder.Default
    @Min(0)
    @Column(name = "first_class_seats", nullable = false)
    private Integer firstClassSeats = 0;

    // Aircraft Performance
    @Min(0)
    @Column(name = "range_km")
    private Integer rangeKm;

    @Min(0)
    @Column(name = "cruising_speed_kmh")
    private Integer cruisingSpeedKmh;

    @Min(0)
    @Column(name = "max_altitude_ft")
    private Integer maxAltitudeFt;

    @Column(name = "year_of_manufacture")
    private Integer yearOfManufacture;

    // Registration & Maintenance
    @Column(name = "registration_date")
    private LocalDate registrationDate;

    @Column(name = "next_maintenance_date")
    private LocalDate nextMaintenanceDate;

    // Status
    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "aircraft_status", nullable = false, length = 30)
    private AircraftStatus aircraftStatus = AircraftStatus.ACTIVE;

    @Builder.Default
    @Column(name = "is_available", nullable = false)
    private Boolean isAvailable = true;

    // Airline Relationship
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "airline_id", nullable = false, foreignKey = @ForeignKey(name = "fk_aircraft_airline"))
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Airline airline;

    // Current Airport
    @Column(name = "current_airport_id")
    private Long currentAirportId;

    // Auditing
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private Instant updatedAt;

    @Version
    @Column(nullable = false)
    private Long version;

    @Transient
    public Integer getTotalSeats() {
        return safeValue(economySeats) + safeValue(premiumEconomySeats)
                + safeValue(businessSeats) + safeValue(firstClassSeats);
    }

    @Transient
    public Boolean getSeatConfigurationValid(){
        return getTotalSeats().equals(seatingCapacity == null ? 0 : seatingCapacity);
    }

    @Transient
    public Boolean getRequiresMaintenance() {
        if(nextMaintenanceDate == null)
            return false;

        return !nextMaintenanceDate.isAfter(LocalDate.now().plusWeeks(2));
    }

    @Transient
    public Boolean getMaintenanceOverdue() {
        if (nextMaintenanceDate == null)
            return false;

        return nextMaintenanceDate.isBefore(LocalDate.now());
    }

    @Transient
    public Boolean getOperational() {
        return AircraftStatus.ACTIVE.equals(aircraftStatus)
                && Boolean.TRUE.equals(isAvailable)
                && !Boolean.TRUE.equals(getMaintenanceOverdue());
    }


    private int safeValue(Integer value){
        return value == null ? 0 : value;
    }


}
