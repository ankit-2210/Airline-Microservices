package com.loyaltyservice.controller;

import com.airlineportal.payload.request.Loyalty.AdjustPointsRequest;
import com.airlineportal.payload.request.Loyalty.EarnPointsRequest;
import com.airlineportal.payload.request.Loyalty.RedeemPointsRequest;
import com.airlineportal.payload.response.ApiResponse;
import com.airlineportal.payload.response.Loyalty.LoyaltyAccountResponse;
import com.airlineportal.payload.response.Loyalty.LoyaltyTransactionResponse;
import com.loyaltyservice.service.LoyaltyService;
import jakarta.validation.Valid;
import lombok.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/loyalty")
public class LoyaltyController {
    private final LoyaltyService loyaltyService;

    @PostMapping("/account/{userId}")
    public ApiResponse<LoyaltyAccountResponse> createAccount(@PathVariable Long userId){
        return ApiResponse.success(loyaltyService.createAccount(userId));
    }

    @GetMapping("/accounts/user/{userId}")
    public ApiResponse<LoyaltyAccountResponse> getAccountByUserId(@PathVariable Long userId){
        return ApiResponse.success(loyaltyService.getAccountByUserId(userId));
    }

    @PostMapping("/points/earn")
    public ApiResponse<LoyaltyAccountResponse> earnPoints(@Valid @RequestBody EarnPointsRequest request){
        return ApiResponse.success(loyaltyService.earnPoints(request));
    }

    @PostMapping("/points/redeem")
    public ApiResponse<LoyaltyAccountResponse> redeemPoints(@Valid @RequestBody RedeemPointsRequest request){
        return ApiResponse.success(loyaltyService.redeemPoints(request));
    }

    @PostMapping("/points/adjust")
    public ApiResponse<LoyaltyAccountResponse> adjustPoints(@Valid @RequestBody AdjustPointsRequest request){
        return ApiResponse.success(loyaltyService.adjustPoints(request));
    }

    @GetMapping("/transactions/user/{userId}")
    public ApiResponse<Page<LoyaltyTransactionResponse>> getTransactionsByUserId(@PathVariable Long userId, @PageableDefault(size = 10) Pageable pageable){
        return ApiResponse.success(loyaltyService.getTransactionsByUserId(userId, pageable));
    }

    @GetMapping("/transactions/account/{accountId}")
    public ApiResponse<Page<LoyaltyTransactionResponse>> getTransactionsByAccountId(@PathVariable Long accountId, @PageableDefault(size = 10) Pageable pageable){
        return ApiResponse.success(loyaltyService.getTransactionsByAccountId(accountId, pageable));
    }



}
