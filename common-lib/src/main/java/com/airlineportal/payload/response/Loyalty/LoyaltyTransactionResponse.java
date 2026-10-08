package com.airlineportal.payload.response.Loyalty;

import com.airlineportal.utils.Loyalty.LoyaltyTransactionType;
import lombok.*;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoyaltyTransactionResponse {
    private Long id;
    private Long loyaltyAccountId;
    private Long userId;
    private Long bookingId;

    private Long points;

    private LoyaltyTransactionType transactionType;

    private String description;

    private Instant createdAt;
    private Instant updatedAt;

}
