package com.flightservice.model;


import com.airlineportal.utils.Flight.FlightStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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
                @Index(name = "idx_instance_schedule", columnList = "schedule_id"),
                @Index(name = "idx_instance_departure", columnList = "departure_date_time"),
                @Index(name = "idx_instance_arrival", columnList = "arrival_date_time"),
                @Index(name = "idx_instance_status", columnList = "flight_status"),
                @Index(name = "idx_instance_active", columnList = "active"),
                @Index(name = "idx_instance_booking", columnList = "flight_id, departure_date_time")
        }
)
@EntityListeners(AuditingEntityListener.class)
public class FlightInstance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Flight
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "flight_id", nullable = false, foreignKey = @ForeignKey(name = "fk_instance_flight"))
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Flight flight;

    // Schedule
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "schedule_id", nullable = false, foreignKey = @ForeignKey(name = "fk_instance_schedule"))
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private FlightSchedule schedule;

    // Date / Time
    @NotNull
    @Column(name = "departure_date_time", nullable = false)
    private LocalDateTime departureDateTime;

    @NotNull
    @Column(name = "arrival_date_time", nullable = false)
    private LocalDateTime arrivalDateTime;

    // Seats
    @NotNull
    @Min(1)
    @Column(name = "total_seats", nullable = false)
    private Integer totalSeats;

    @NotNull
    @Min(0)
    @Column(name = "available_seats", nullable = false)
    private Integer availableSeats;

    // Status
    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "flight_status", nullable = false, length = 30)
    private FlightStatus flightStatus = FlightStatus.SCHEDULED;

    // Booking Rules
    @Min(0)
    @Column(name = "min_advance_booking_days")
    private Integer minAdvanceBookingDays;

    @Min(0)
    @Column(name = "max_advance_booking_days")
    private Integer maxAdvanceBookingDays;

    // Status
    @Builder.Default
    @Column(name = "active", nullable = false)
    private Boolean active=true;

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
    public String getFormatedDuration(){
        if(departureDateTime == null || arrivalDateTime == null){
            return "0h 0m";
        }

        Duration duration = Duration.between(departureDateTime, arrivalDateTime);
        return duration.toHours() + "h " + duration.toMinutesPart() + "m";
    }

    @Transient
    public Integer getBookedSeats(){
        int total = totalSeats == null ? 0 : totalSeats;
        int available = availableSeats == null ? 0 : availableSeats;
        return Math.max(0, total - available);
    }

    @Transient
    public Boolean isSoldOut(){
        return availableSeats != null && availableSeats <= 0;
    }


    @Transient
    public Boolean isDeparted() {
        return FlightStatus.DEPARTED.equals(flightStatus) || FlightStatus.ARRIVED.equals(flightStatus);
    }

    @Transient
    public Boolean isArrived() {
        return FlightStatus.ARRIVED.equals(flightStatus);
    }

    @Transient
    public Boolean isCancelled() {
        return FlightStatus.CANCELLED.equals(flightStatus);
    }

    @Transient
    public Boolean isBookingOpen() {
        if(!Boolean.TRUE.equals(active))
            return false;
        if(departureDateTime == null)
            return false;
        if(isSoldOut())
            return false;
        if(isCancelled())
            return false;

        return LocalDateTime.now().isBefore(departureDateTime);
    }

    @Transient
    public Boolean canCheckIn(){
        if(departureDateTime == null){
            return false;
        }
        if(isCancelled() || isDeparted())
            return false;

        LocalDateTime now = LocalDateTime.now();
        return !now.isBefore(departureDateTime.minusHours(24)) && now.isBefore(departureDateTime);
    }


    @Transient
    public Boolean isActive() {
        return Boolean.TRUE.equals(active) && !isCancelled();
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
