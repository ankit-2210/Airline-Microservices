package com.airlineportal.payload.response.Loyalty;

import com.airlineportal.utils.Loyalty.LoyaltyTier;
import lombok.*;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoyaltyAccountResponse {
    private Long id;
    private Long userId;

    private LoyaltyTier loyaltyTier;

    private Long totalPoints;
    private Long availablePoints;
    private Long lifetimePoints;

    private Instant createdAt;
    private Instant updatedAt;


}
