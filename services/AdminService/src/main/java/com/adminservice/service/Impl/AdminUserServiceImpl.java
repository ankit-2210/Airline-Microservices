package com.adminservice.service.Impl;


import com.adminservice.service.AdminUserService;
import com.airlineportal.client.UserFeignClient;
import com.airlineportal.payload.response.ApiResponse;
import com.airlineportal.payload.response.User.UserResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {
    private final UserFeignClient userFeignClient;

    @Override
    @Retry(name = "userRetry", fallbackMethod = "userFallback")
    @CircuitBreaker(name = "userCB", fallbackMethod = "userFallback")
    public ApiResponse<UserResponse> getUserById(Long userId) {
        return userFeignClient.getUserById(userId);
    }

    @Override
    @Retry(name = "userRetry", fallbackMethod = "userListFallback")
    @CircuitBreaker(name = "userCB", fallbackMethod = "userListFallback")
    public ApiResponse<List<UserResponse>> getAllUsers() {
        return userFeignClient.getAllUsers();
    }


    public ApiResponse<UserResponse> userFallback(Long userId, Throwable ex) {
        return ApiResponse.failure("User service is currently unavailable.");
    }

    public ApiResponse<List<UserResponse>> userListFallback(Throwable ex) {
        return ApiResponse.failure("User service is currently unavailable.");
    }


}
