package com.loyaltyservice.mapper;


import com.airlineportal.payload.request.Loyalty.EarnPointsRequest;
import com.airlineportal.payload.response.Loyalty.LoyaltyAccountResponse;
import com.airlineportal.payload.response.Loyalty.LoyaltyTransactionResponse;
import com.airlineportal.utils.Loyalty.LoyaltyTier;
import com.airlineportal.utils.Loyalty.LoyaltyTransactionType;
import com.loyaltyservice.dto.LoyaltyReportData;
import com.loyaltyservice.model.LoyaltyAccount;
import com.loyaltyservice.model.LoyaltyTransaction;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public final class LoyaltyMapper {

    public static LoyaltyAccount toEntity(Long userId){
        return LoyaltyAccount.builder()
                .userId(userId)

                .loyaltyTier(LoyaltyTier.BRONZE)
                .totalPoints(0L)
                .availablePoints(0L)
                .lifetimePoints(0L)

                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

    }

    public static LoyaltyTransaction toEntityTransaction(LoyaltyAccount savedAccount, EarnPointsRequest request, long points) {
        return LoyaltyTransaction.builder()
                .loyaltyAccountId(savedAccount.getId())
                .userId(request.getUserId())
                .bookingId(request.getBookingId())

                .points(points)
                .transactionType(LoyaltyTransactionType.EARNED)
                .description("Points earned from booking " + request.getBookingId())

                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
    }


    public static LoyaltyAccountResponse toAccountResponse(LoyaltyAccount account){
        if(account == null)
            return null;

        return LoyaltyAccountResponse.builder()
                .id(account.getId())
                .userId(account.getUserId())

                .loyaltyTier(account.getLoyaltyTier())

                .totalPoints(account.getTotalPoints())
                .availablePoints(account.getAvailablePoints())
                .lifetimePoints(account.getLifetimePoints())

                .createdAt(account.getCreatedAt())
                .updatedAt(account.getUpdatedAt())
                .build();
    }

    public static LoyaltyTransactionResponse toTransactionResponse(LoyaltyTransaction transaction) {
        if (transaction == null)
            return null;

        return LoyaltyTransactionResponse.builder()
                .id(transaction.getId())
                .loyaltyAccountId(transaction.getLoyaltyAccountId())
                .userId(transaction.getUserId())
                .bookingId(transaction.getBookingId())

                .points(transaction.getPoints())
                .transactionType(transaction.getTransactionType())
                .description(transaction.getDescription())

                .createdAt(transaction.getCreatedAt())
                .updatedAt(transaction.getUpdatedAt())
                .build();
    }

    public static LoyaltyReportData toReportData(LoyaltyAccount account){
        if(account == null)
            return null;

        return LoyaltyReportData.builder()
                .id(account.getId())
                .userId(account.getUserId())

                .loyaltyTier(account.getLoyaltyTier() != null ? account.getLoyaltyTier().name() : "-")

                .totalPoints(account.getTotalPoints())
                .availablePoints(account.getAvailablePoints())
                .lifetimePoints(account.getLifetimePoints())

                .createdAt(account.getCreatedAt() != null ? account.getCreatedAt().toString() : "-")
                .updatedAt(account.getUpdatedAt() != null ? account.getUpdatedAt().toString() : "-")
                .build();

    }


}
