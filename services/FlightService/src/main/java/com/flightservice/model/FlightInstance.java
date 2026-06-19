package com.flightservice.model;


import com.microservices.utils.Flight.FlightStatus;
import jakarta.persistence.*;
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
        name = "flight_instances",
        indexes = {
                @Index(name = "idx_instance_flight", columnList = "flight_id"),
                @Index(name = "idx_instance_departure", columnList = "departure_date_time"),
                @Index(name = "idx_instance_status", columnList = "flight_status")
        }
)
@EntityListeners(AuditingEntityListener.class)
public class FlightInstance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flight_id", nullable = false)
    private Flight flight;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", nullable = false)
    private FlightSchedule schedule;

    @Column(name = "departure_date_time", nullable = false)
    private LocalDateTime departureDateTime;

    @Column(name = "arrival_date_time", nullable = false)
    private LocalDateTime arrivalDateTime;

    @Column(name = "total_seats", nullable = false)
    private Integer totalSeats;

    @Column(name = "available_seats", nullable = false)
    private Integer availableSeats;

    @Enumerated(EnumType.STRING)
    @Column(name = "flight_status", nullable = false)
    private FlightStatus flightStatus;

    private Integer minAdvanceBookingDays;
    private Integer maxAdvanceBookingDays;

    @Builder.Default
    @Column(nullable = false)
    private Boolean active=true;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Transient
    public String getFormatedDuration(){
        Duration duration = Duration.between(departureDateTime, arrivalDateTime);
        return duration.toHours() + "h " + duration.toMinutesPart() + "m";
    }

    @Transient
    public Integer getBookedSeats(){
        return totalSeats - availableSeats;
    }

    @Transient
    public Boolean isSoldOut(){
        return availableSeats <= 0;
    }

    @Transient
    public Boolean isBookingOpen(){
        if(departureDateTime == null)
            return false;
        return LocalDateTime.now().isBefore(departureDateTime);
    }

    @Transient
    public Boolean canCheckIn(){
        return LocalDateTime.now().isAfter(departureDateTime.minusMinutes(24));
    }

    @Transient
    public Boolean isDeparted() {
        return FlightStatus.DEPARTED.equals(flightStatus);
    }

    @Transient
    public Boolean isArrived() {
        return FlightStatus.ARRIVED.equals(flightStatus);
    }

    @Transient
    public Boolean isCancelled() {
        return FlightStatus.CANCELLED.equals(flightStatus);
    }
}

// flights
//-------
//AI101
//DEL -> BOM


//flight_schedules
//----------------
//AI101
//MON WED FRI
//08:00


//flight_instances
//----------------
//AI101
//2026-08-15 08:00
//180 seats
