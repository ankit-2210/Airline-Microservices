package com.loyaltyservice.mapper;


import com.airlineportal.payload.response.Loyalty.LoyaltyAccountResponse;
import com.airlineportal.payload.response.Loyalty.LoyaltyTransactionResponse;
import com.loyaltyservice.model.LoyaltyAccount;
import com.loyaltyservice.model.LoyaltyTransaction;
import org.springframework.stereotype.Component;

@Component
public class LoyaltyMapper {

    public LoyaltyAccountResponse toAccountResponse(LoyaltyAccount account){
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

    public LoyaltyTransactionResponse toTransactionResponse(LoyaltyTransaction transaction) {
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

}
