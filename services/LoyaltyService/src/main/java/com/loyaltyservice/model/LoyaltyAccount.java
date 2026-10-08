package com.loyaltyservice.model;

import com.airlineportal.utils.Loyalty.LoyaltyTier;
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
        name = "loyalty_accounts",
        indexes = {
                @Index(name = "idx_loyalty_user_id", columnList = "user_id"),
                @Index(name = "idx_loyalty_tier", columnList = "loyalty_tier")
        }
)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class LoyaltyAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "loyalty_tier", nullable = false, length = 30)
    @Builder.Default
    private LoyaltyTier loyaltyTier = LoyaltyTier.BRONZE;

    @Column(name = "total_points", nullable = false)
    @Builder.Default
    private Long totalPoints = 0L;

    @Column(name = "available_points", nullable = false)
    @Builder.Default
    private Long availablePoints = 0L;

    @Column(name = "lifetime_points", nullable = false)
    @Builder.Default
    private Long lifetimePoints = 0L;

    @Column(name = "created_at", nullable = false)
    @Builder.Default
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at")
    private Instant updatedAt;


}
