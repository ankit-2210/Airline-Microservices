package com.checkinservice.model;

import com.airlineportal.utils.CheckIn.CheckInStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "check_ins",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_checkin_booking_passenger",
                        columnNames = {"booking_id", "passenger_id"}
                )
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckIn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "booking_id", nullable = false)
    private Long bookingId;

    @Column(name = "passenger_id", nullable = false)
    private Long passengerId;

    @Column(name = "flight_instance_id", nullable = false)
    private Long flightInstanceId;

    @Column(nullable = false)
    private String pnr;

    @Column(name = "seat_number", nullable = false)
    private String seatNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CheckInStatus status;

    private LocalDateTime checkedInAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }



}
