package com.airlineportal.client;


import com.airlineportal.payload.response.ApiResponse;
import com.airlineportal.payload.response.Loyalty.LoyaltyAccountResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "LOYALTYSERVICE",
        path = "/internal/loyalty"
)
public interface LoyaltyFeignClient {

    @GetMapping("/users/{userId}/account")
    ApiResponse<LoyaltyAccountResponse> getAccountByUserId(@PathVariable Long userId);



}
