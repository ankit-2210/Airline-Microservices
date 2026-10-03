package com.fareservice.model;

import com.airlineportal.utils.Fare.FareClass;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "fares",
        indexes = {
                @Index(name = "idx_fare_flight_instance", columnList = "flight_instance_id"),
                @Index(name = "idx_fare_flight", columnList = "flight_id"),
                @Index(name = "idx_fare_class", columnList = "fare_class")
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Fare {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "flight_id", nullable = false)
    private Long flightId;

    @Column(name = "flight_instance_id", nullable = false)
    private Long flightInstanceId;

    @Enumerated(EnumType.STRING)
    @Column(name = "fare_class", nullable = false)
    private FareClass fareClass;

    @Column(name = "base_fare", nullable = false, precision = 12, scale = 2)
    private BigDecimal baseFare;

    @Column(name = "tax", nullable = false, precision = 12, scale = 2)
    private BigDecimal tax;

    @Column(name = "cancellation_fee", precision = 12, scale = 2)
    private BigDecimal cancellationFee;

    @Column(name = "change_fee", precision = 12, scale = 2)
    private BigDecimal changeFee;

    @Column(name = "active", nullable = false)
    private boolean active = true;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();

        if (createdAt == null) {
            createdAt = now;
        }

        if (tax == null) {
            tax = BigDecimal.ZERO;
        }

        if (cancellationFee == null) {
            cancellationFee = BigDecimal.ZERO;
        }

        if (changeFee == null) {
            changeFee = BigDecimal.ZERO;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public BigDecimal getTotalFare() {
        return baseFare.add(tax);
    }




}















