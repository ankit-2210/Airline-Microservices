package com.loyaltyservice.model;

import com.airlineportal.utils.Loyalty.LoyaltyTransactionType;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "loyalty_transactions",
        indexes = {
                @Index(name = "idx_loyalty_tx_account", columnList = "loyalty_account_id"),
                @Index(name = "idx_loyalty_tx_user", columnList = "user_id"),
                @Index(name = "idx_loyalty_tx_booking", columnList = "booking_id"),
                @Index(name = "idx_loyalty_tx_type", columnList = "transaction_type")
        }
)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class LoyaltyTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "loyalty_account_id", nullable = false)
    private Long loyaltyAccountId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "booking_id")
    private Long bookingId;

    @Column(name = "points", nullable = false)
    private Long points;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false, length = 30)
    private LoyaltyTransactionType transactionType;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "created_at", nullable = false)
    @Builder.Default
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at")
    private Instant updatedAt;



}
