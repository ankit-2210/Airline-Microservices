package com.flightservice.model;

import com.airlineportal.utils.Flight.FlightStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(
        name = "flight",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_flight_number", columnNames = "flight_number")
        },
        indexes = {
                @Index(name = "idx_flight_number", columnList = "flight_number"),
                @Index(name = "idx_flight_airline", columnList = "airline_id"),
                @Index(name = "idx_flight_aircraft", columnList = "aircraft_id"),
                @Index(name = "idx_flight_departure_airport", columnList = "departure_airport_id"),
                @Index(name = "idx_flight_arrival_airport", columnList = "arrival_airport_id"),
                @Index(name = "idx_flight_status", columnList = "flight_status"),
                @Index(name = "idx_flight_active", columnList = "active"),
                @Index(name = "idx_flight_scheduled_departure", columnList = "scheduled_departure")
        }
)
@EntityListeners(AuditingEntityListener.class)
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Flight Identification
    @NotBlank
    @Size(max = 20)
    @Column(name = "flight_number", nullable = false, unique = true, length = 20)
    private String flightNumber;

    // Airline / Aircraft
    @NotNull
    @Column(name = "airline_id", nullable = false)
    private Long airlineId;

    @NotNull
    @Column(name = "aircraft_id", nullable = false)
    private Long aircraftId;

    // Route
    @NotNull
    @Column(name = "departure_airport_id", nullable = false)
    private Long departureAirportId;

    @NotNull
    @Column(name = "arrival_airport_id", nullable = false)
    private Long arrivalAirportId;

    // Schedule
    @NotNull
    @Column(name = "scheduled_departure", nullable = false)
    private LocalDateTime scheduledDeparture;

    @NotNull
    @Column(name = "scheduled_arrival", nullable = false)
    private LocalDateTime scheduledArrival;

    // Actual Operation
    @Column(name = "actual_departure")
    private LocalDateTime actualDeparture;

    @Column(name = "actual_arrival")
    private LocalDateTime actualArrival;

    // Status
    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "flight_status", nullable = false, length = 30)
    private FlightStatus flightStatus = FlightStatus.SCHEDULED;

    @Builder.Default
    @Column(name = "active", nullable = false)
    private Boolean active = true;

    // Auditing
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Version
    @Column(nullable = false)
    private Long version;


    // Computed Properties
    @Transient
    public Boolean getDelayed() {
        if(actualDeparture == null || scheduledDeparture == null){
            return false;
        }

        return actualDeparture.isAfter(scheduledDeparture);
    }

    @Transient
    public Long getDelayMinutes(){
        if(!Boolean.TRUE.equals(getDelayed()))
            return 0L;
        return Duration.between(scheduledDeparture, actualDeparture).toMinutes();
    }

    @Transient
    public Long getScheduledDurationMinutes(){
        if(scheduledDeparture == null || scheduledArrival == null){
            return 0L;
        }
        return Duration.between(scheduledDeparture, scheduledArrival).toMinutes();
    }

    @Transient
    public Boolean getDeparted(){
        return actualDeparture != null ||
                FlightStatus.DEPARTED.equals(flightStatus) || FlightStatus.ARRIVED.equals(flightStatus);
    }

    @Transient
    public Boolean getArrived(){
        return actualArrival != null || FlightStatus.ARRIVED.equals(flightStatus);
    }

    @Transient
    public Boolean getCancelled(){
        return FlightStatus.CANCELLED.equals(flightStatus);
    }

    @Transient
    public Boolean getOperational() {
        return Boolean.TRUE.equals(active) && !Boolean.TRUE.equals(getCancelled());
    }

}


//Flight
//        AI101
//DEL → BOM
//
//        ↓
//
//FlightSchedule
//MON / WED / FRI
//08:00 → 10:15
//        01-Aug → 31-Dec
//
//        ↓
//
//FlightInstance
//        AI101
//24-Aug-2026 08:00
//        24-Aug-2026 10:15
//        180 seats
