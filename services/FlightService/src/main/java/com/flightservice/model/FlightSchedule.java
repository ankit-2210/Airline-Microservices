package com.flightservice.model;

import jakarta.persistence.*;
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
                @Index(name = "idx_schedule_active", columnList = "active")
        }
)
@EntityListeners(AuditingEntityListener.class)
public class FlightSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "flight_id", nullable = false)
    private Flight flight;

    @Column(name = "departure_time", nullable = false)
    private LocalTime departureTime;

    @Column(name = "arrival_time", nullable = false)
    private LocalTime arrivalTime;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "schedule_operating_days",
            joinColumns = @JoinColumn(name = "schedule_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "operating_day")
    @Builder.Default
    private List<DayOfWeek> operatingDays = new ArrayList<>();

    @Builder.Default
    @Column(name = "active", nullable = false)
    private Boolean active=true;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Transient
    public Boolean isOperatingOn(LocalDate date){
        return date != null &&
                !date.isBefore(startDate) && !date.isAfter(endDate)
                && operatingDays.contains(date.getDayOfWeek());
    }

    @Transient
    public LocalDateTime getDepartureDateTime(LocalDate date){
        return LocalDateTime.of(date, departureTime);
    }

    @Transient
    public LocalDateTime getArrivalDateTime(LocalDate date){
        return LocalDateTime.of(date, arrivalTime);
    }

}
