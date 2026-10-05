package com.baggageservice.model;


import com.airlineportal.utils.Baggage.BaggageStatus;
import com.airlineportal.utils.Baggage.BaggageType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(
        name = "baggage",
        indexes = {
                @Index(name = "idx_baggage_booking", columnList = "booking_id"),
                @Index(name = "idx_baggage_passenger", columnList = "passenger_id"),
                @Index(name = "idx_baggage_flight_instance", columnList = "flight_instance_id")
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Baggage {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "booking_id", nullable = false)
        private Long bookingId;

        @Column(name = "passenger_id", nullable = false)
        private Long passengerId;

        @Column(name = "flight_instance_id", nullable = false)
        private Long flightInstanceId;

        @Enumerated(EnumType.STRING)
        @Column(name = "baggage_type", nullable = false)
        private BaggageType baggageType;

        @Column(nullable = false, precision = 10, scale = 2)
        private BigDecimal weight;

        @Column(nullable = false)
        private Integer quantity;

        @Column(nullable = false, precision = 12, scale = 2)
        private BigDecimal price;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private BaggageStatus status;

        @Column(nullable = false, updatable = false)
        private Instant createdAt;

        private Instant updatedAt;

        @PrePersist
        protected void onCreate() {
                createdAt = Instant.now();

                if (status == null) {
                        status = BaggageStatus.ADDED;
                }
        }

        @PreUpdate
        protected void onUpdate() {
                updatedAt = Instant.now();
        }

}
