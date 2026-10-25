package com.notificationservice.external;


import com.airlineportal.client.UserFeignClient;
import com.airlineportal.payload.response.ApiResponse;
import com.airlineportal.payload.response.User.UserResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExternalServiceImpl implements ExternalService{
    private final UserFeignClient userFeignClient;

    // User
    @Override
    @Retry(name = "userRetry", fallbackMethod = "userFallback")
    @CircuitBreaker(name = "userCB", fallbackMethod = "userFallback")
    public ApiResponse<UserResponse> getUserById(Long userId) {
        return userFeignClient.getUserById(userId);
    }


    // FALLBACK METHODS
    public ApiResponse<UserResponse> userFallback(Long userId, Throwable t){
        return ApiResponse.failure("User Service is unavailable.");
    }

}
