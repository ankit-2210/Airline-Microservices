package com.loyaltyservice.helper;


import com.airlineportal.utils.Loyalty.LoyaltyTier;
import com.loyaltyservice.model.LoyaltyAccount;
import com.loyaltyservice.repository.LoyaltyAccountRepository;
import com.loyaltyservice.repository.LoyaltyTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
@RequiredArgsConstructor
public class LoyaltyHelper {
    private final LoyaltyAccountRepository loyaltyAccountRepository;
    private final LoyaltyTransactionRepository loyaltyTransactionRepository;

    private static final BigDecimal POINT_VALUE = BigDecimal.valueOf(100);

    public LoyaltyAccount findAccountByUserId(Long userId) {
        return loyaltyAccountRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Loyalty account not found for user ID: " + userId));
    }

    public long calculateEarnedPoints(BigDecimal bookingAmount){
        if(bookingAmount == null || bookingAmount.compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("Booking amount must be greater than zero");
        }

        return bookingAmount.divide(POINT_VALUE, 0, RoundingMode.DOWN)
                .longValue();
    }

    public LoyaltyTier calculateTier(long lifetimePoints){
        if(lifetimePoints >= 10000)
            return LoyaltyTier.PLATINUM;
        if(lifetimePoints >= 5000)
            return LoyaltyTier.GOLD;
        if(lifetimePoints >= 2000)
            return LoyaltyTier.SILVER;

        return LoyaltyTier.BRONZE;
    }




}
