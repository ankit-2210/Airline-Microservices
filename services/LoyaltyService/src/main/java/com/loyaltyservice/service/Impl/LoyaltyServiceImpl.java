package com.loyaltyservice.service.Impl;

import com.airlineportal.payload.request.Loyalty.AdjustPointsRequest;
import com.airlineportal.payload.request.Loyalty.EarnPointsRequest;
import com.airlineportal.payload.request.Loyalty.RedeemPointsRequest;
import com.airlineportal.payload.response.Loyalty.LoyaltyAccountResponse;
import com.airlineportal.payload.response.Loyalty.LoyaltyTransactionResponse;
import com.loyaltyservice.service.LoyaltyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoyaltyServiceImpl implements LoyaltyService {


    @Override
    public LoyaltyAccountResponse createAccount(Long userId) {
        return null;
    }

    @Override
    public LoyaltyAccountResponse getAccountByUserId(Long userId) {
        return null;
    }

    @Override
    public LoyaltyAccountResponse earnPoints(EarnPointsRequest request) {
        return null;
    }

    @Override
    public LoyaltyAccountResponse redeemPoints(RedeemPointsRequest request) {
        return null;
    }

    @Override
    public LoyaltyAccountResponse adjustPoints(AdjustPointsRequest request) {
        return null;
    }

    @Override
    public Page<LoyaltyTransactionResponse> getTransactionsByUserId(Long userId, Pageable pageable) {
        return null;
    }

    @Override
    public Page<LoyaltyTransactionResponse> getTransactionsByAccountId(Long accountId, Pageable pageable) {
        return null;
    }
}
