package com.loyaltyservice.service;

import com.airlineportal.payload.request.Loyalty.AdjustPointsRequest;
import com.airlineportal.payload.request.Loyalty.EarnPointsRequest;
import com.airlineportal.payload.request.Loyalty.RedeemPointsRequest;
import com.airlineportal.payload.response.Loyalty.LoyaltyAccountResponse;
import com.airlineportal.payload.response.Loyalty.LoyaltyTransactionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LoyaltyService {

    LoyaltyAccountResponse createAccount(Long userId);
    LoyaltyAccountResponse getAccountByUserId(Long userId);
    LoyaltyAccountResponse earnPoints(EarnPointsRequest request);
    LoyaltyAccountResponse redeemPoints(RedeemPointsRequest request);
    LoyaltyAccountResponse adjustPoints(AdjustPointsRequest request);

    Page<LoyaltyTransactionResponse> getTransactionsByUserId(Long userId, Pageable pageable);
    Page<LoyaltyTransactionResponse> getTransactionsByAccountId(Long accountId, Pageable pageable);



}
