package com.loyaltyservice.internal;

import com.airlineportal.payload.response.ApiResponse;
import com.airlineportal.payload.response.Loyalty.LoyaltyAccountResponse;
import com.loyaltyservice.service.LoyaltyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/loyalty")
public class InternalLoyaltyController {
    private final LoyaltyService loyaltyService;

    @GetMapping("/users/{userId}/account")
    public ApiResponse<LoyaltyAccountResponse> getAccountByUserId(@PathVariable Long userId) {
        return ApiResponse.success(loyaltyService.getAccountByUserId(userId));
    }



}
