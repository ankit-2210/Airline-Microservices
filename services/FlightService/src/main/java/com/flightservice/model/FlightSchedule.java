package com.flightservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.*;
import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(
        name = "flight_schedules",
        indexes = {
                @Index(name = "idx_schedule_flight", columnList = "flight_id"),
                @Index(name = "idx_schedule_active", columnList = "active"),
                @Index(name = "idx_schedule_start_date", columnList = "start_date"),
                @Index(name = "idx_schedule_end_date", columnList = "end_date")
        }
)
@EntityListeners(AuditingEntityListener.class)
public class FlightSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Flight
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "flight_id", nullable = false, foreignKey = @ForeignKey(name = "fk_schedule_flight"))
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Flight flight;

    // Time
    @NotNull
    @Column(name = "departure_time", nullable = false)
    private LocalTime departureTime;

    @NotNull
    @Column(name = "arrival_time", nullable = false)
    private LocalTime arrivalTime;

    // Validity Period
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    // Operating Days
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "schedule_operating_days",
            joinColumns = @JoinColumn(name = "schedule_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "operating_day", nullable = false, length = 15)
    @Builder.Default
    private Set<DayOfWeek> operatingDays = new HashSet<>();

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
    public Boolean isOperatingOn(LocalDate date){
        if(date == null || startDate == null || endDate == null){
            return false;
        }
        if(!Boolean.TRUE.equals(active)){
            return false;
        }

        return !date.isBefore(startDate) && !date.isAfter(endDate)
                && operatingDays.contains(date.getDayOfWeek());
    }

    @Transient
    public LocalDateTime getDepartureDateTime(LocalDate date){
        if(date == null || departureTime == null)
            return null;
        return LocalDateTime.of(date, departureTime);
    }

    @Transient
    public LocalDateTime getArrivalDateTime(LocalDate date){
        if(date == null || arrivalTime == null || departureTime == null)
            return null;

        LocalDateTime arrival =  LocalDateTime.of(date, arrivalTime);
        if(arrivalTime.isBefore(departureTime)){
            arrival = arrival.plusDays(1);
        }
        return arrival;
    }

    @Transient
    public Boolean isCurrentlyActive() {
        LocalDate today = LocalDate.now();
        return Boolean.TRUE.equals(active) && !today.isBefore(startDate) && !today.isAfter(endDate);
    }

}
