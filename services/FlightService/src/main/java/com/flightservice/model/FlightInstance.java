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
import java.util.*;

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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "flight_id", nullable = false)
    private Flight flight;

    @Column(name = "departure_date_time", nullable = false)
    private LocalDateTime departureDateTime;

    @Column(name = "arrival_date_time", nullable = false)
    private LocalDateTime arrivalDateTime;

    @Column(nullable = false)
    private Integer totalSeats;

    @Column(nullable = false)
    private Integer availableSeats;

    @Enumerated(EnumType.STRING)
    @Column(name = "flight_status", nullable = false)
    private FlightStatus flightStatus;

    private Integer minAdvanceBookingDays;
    private Integer maxAdvanceBookingDays;

    @Builder.Default
    private Boolean active=true;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private Instant updatedAt;

    @Transient
    public String getFormatedDuration(){
        if(departureDateTime == null || arrivalDateTime == null)
            return null;

        Duration duration = Duration.between(departureDateTime, arrivalDateTime);
        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();

        StringBuilder sb = new StringBuilder();
        if(hours>0){
            sb.append(hours).append("h ");
        }
        if(minutes>0){
            sb.append(minutes).append("m");
        }
        return sb.toString().trim();
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
        if(departureDateTime == null)
            return false;
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
