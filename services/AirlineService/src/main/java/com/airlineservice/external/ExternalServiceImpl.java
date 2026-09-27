package com.airlineservice.external;

import com.airlineportal.client.LocationFeignClient;
import com.airlineportal.client.UserFeignClient;
import com.airlineportal.payload.response.ApiResponse;
import com.airlineportal.payload.response.Location.Airport.AirportResponse;
import com.airlineportal.payload.response.Location.City.CityResponse;
import com.airlineportal.payload.response.User.UserResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExternalServiceImpl implements ExternalService {
    private final UserFeignClient userFeignClient;
    private final LocationFeignClient locationFeignClient;


    @Override
    @Retry(name = "userRetry", fallbackMethod = "userFallback")
    @CircuitBreaker(name = "userCB", fallbackMethod = "userFallback")
    public ApiResponse<UserResponse> getUserById(Long userId) {
        return userFeignClient.getUserById(userId);
    }

    @Override
    @Retry(name = "locationRetry", fallbackMethod = "cityFallback")
    @CircuitBreaker(name = "locationCB", fallbackMethod = "cityFallback")
    public ApiResponse<CityResponse> getCityById(Long cityId) {
        return locationFeignClient.getCityById(cityId);
    }

    @Override
    @Retry(name = "locationRetry", fallbackMethod = "airportFallback")
    @CircuitBreaker(name = "locationCB", fallbackMethod = "airportFallback")
    public ApiResponse<AirportResponse> getAirportById(Long airportId) {
        return locationFeignClient.getAirportById(airportId);
    }


    // FALLBACK METHODS
    public ApiResponse<UserResponse> userFallback(Long userId, Throwable t){
        return ApiResponse.failure("User Service is unavailable.");
    }

    public ApiResponse<CityResponse> cityFallback(Long cityId, Throwable t){
        return ApiResponse.failure("Location Service is unavailable.");
    }

    public ApiResponse<AirportResponse> airportFallback(Long airportId, Throwable t){
        return ApiResponse.failure("Location Service is unavailable.");
    }

}
