package com.loyaltyservice.repository;

import com.airlineportal.utils.Loyalty.LoyaltyTransactionType;
import com.loyaltyservice.model.LoyaltyTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoyaltyTransactionRepository extends JpaRepository<LoyaltyTransaction, Long> {

    Page<LoyaltyTransaction> findByUserId(Long userId, Pageable pageable);
    Page<LoyaltyTransaction> findByLoyaltyAccountId(Long loyaltyAccountId, Pageable pageable);
    Page<LoyaltyTransaction> findByTransactionType(LoyaltyTransactionType transactionType, Pageable pageable);

    boolean existsByBookingIdAndTransactionType(Long bookingId, LoyaltyTransactionType transactionType);


}
