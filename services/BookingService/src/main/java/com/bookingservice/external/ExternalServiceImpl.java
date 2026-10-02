package com.bookingservice.external;

import com.airlineportal.client.FlightFeignClient;
import com.airlineportal.client.UserFeignClient;
import com.airlineportal.payload.response.ApiResponse;
import com.airlineportal.payload.response.Flight.FlightInstanceResponse;
import com.airlineportal.payload.response.Flight.FlightResponse;
import com.airlineportal.payload.response.User.UserResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExternalServiceImpl implements ExternalService{
    private final UserFeignClient userFeignClient;
    private final FlightFeignClient flightFeignClient;

    // User
    @Override
    @Retry(name = "userRetry", fallbackMethod = "userFallback")
    @CircuitBreaker(name = "userCB", fallbackMethod = "userFallback")
    public ApiResponse<UserResponse> getUserById(Long userId) {
        return userFeignClient.getUserById(userId);
    }

    // Flight
    @Override
    @Retry(name = "flightRetry", fallbackMethod = "flightFallback")
    @CircuitBreaker(name = "flightCB", fallbackMethod = "flightFallback")
    public ApiResponse<FlightResponse> getFlightById(Long flightId) {
        return flightFeignClient.getFlightById(flightId);
    }

    // Flight Instance
    @Override
    @Retry(name = "flightRetry", fallbackMethod = "flightInstanceFallback")
    @CircuitBreaker(name = "flightCB", fallbackMethod = "flightInstanceFallback")
    public ApiResponse<FlightInstanceResponse> getInstanceById(Long instanceId) {
        return flightFeignClient.getInstanceById(instanceId);
    }

    @Override
    @Retry(name = "flightRetry", fallbackMethod = "reserveSeatsFallback")
    @CircuitBreaker(name = "flightCB", fallbackMethod = "reserveSeatsFallback")
    public ApiResponse<Boolean> reserveSeats(Long instanceId, Integer seats) {
        return flightFeignClient.reserveSeats(instanceId, seats);
    }

    @Override
    @Retry(name = "flightRetry", fallbackMethod = "releaseSeatsFallback")
    @CircuitBreaker(name = "flightCB", fallbackMethod = "releaseSeatsFallback")
    public ApiResponse<Boolean> releaseSeats(Long instanceId, Integer seats) {
        return flightFeignClient.releaseSeats(instanceId, seats);
    }


    // FALLBACK METHODS
    public ApiResponse<UserResponse> userFallback(Long userId, Throwable t){
        return ApiResponse.failure("User Service is unavailable.");
    }

    public ApiResponse<FlightResponse> flightFallback(Long flightId, Throwable t){
        return ApiResponse.failure("Flight Service is unavailable.");
    }


    public ApiResponse<FlightInstanceResponse> flightInstanceFallback(Long instanceId, Throwable throwable){
        return ApiResponse.failure("Flight Service is currently unavailable.");
    }

    public ApiResponse<Boolean> reserveSeatsFallback(Long instanceId, Integer seats, Throwable throwable){
        return ApiResponse.failure("Flight Service is currently unavailable. " + "Seats could not be reserved.");
    }

    public ApiResponse<Boolean> releaseSeatsFallback(Long instanceId, Integer seats, Throwable throwable){
        return ApiResponse.failure("Flight Service is currently unavailable. " + "Seats could not be released.");
    }

}
