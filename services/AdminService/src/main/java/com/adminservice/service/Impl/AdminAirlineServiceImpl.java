package com.adminservice.service.Impl;


import com.adminservice.service.AdminAirlineService;
import com.airlineportal.client.AirlineFeignClient;
import com.airlineportal.payload.response.Airlines.Aircraft.AircraftResponse;
import com.airlineportal.payload.response.Airlines.Airline.AirlineResponse;
import com.airlineportal.payload.response.ApiResponse;
import com.airlineportal.utils.Airline.AirlineStatus;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Service
@RequiredArgsConstructor
public class AdminAirlineServiceImpl implements AdminAirlineService {
    private final AirlineFeignClient airlineFeignClient;

    @Override
    @Retry(name = "airlineRetry", fallbackMethod = "airlineFallback")
    @CircuitBreaker(name = "airlineCB", fallbackMethod = "airlineFallback")
    public ApiResponse<AirlineResponse> getAirlineById(Long airlineId) {
        return airlineFeignClient.getAirlineById(airlineId);
    }

    @Override
    @Retry(name = "airlineRetry", fallbackMethod = "aircraftFallback")
    @CircuitBreaker(name = "airlineCB", fallbackMethod = "aircraftFallback")
    public ApiResponse<AircraftResponse> getAircraftById(Long aircraftId) {
        return airlineFeignClient.getAircraftById(aircraftId);
    }

    @Override
    @Retry(name = "airlineRetry", fallbackMethod = "ownershipFallback")
    @CircuitBreaker(name = "airlineCB", fallbackMethod = "ownershipFallback")
    public ApiResponse<Boolean> aircraftBelongsToAirline(Long aircraftId, Long airlineId) {
        return airlineFeignClient.aircraftBelongsToAirline(aircraftId, airlineId);
    }

    @Override
    @Retry(name = "airlineRetry", fallbackMethod = "airlineFallback")
    @CircuitBreaker(name = "airlineCB", fallbackMethod = "airlineFallback")
    public ApiResponse<AirlineResponse> changeAirlineStatus(@PathVariable Long airlineId, @RequestParam AirlineStatus airlineStatus){
        return airlineFeignClient.changeStatus(airlineId, airlineStatus);
    }




    public ApiResponse<AirlineResponse> airlineFallback(Long airlineId, Throwable ex) {
        return ApiResponse.failure("Airline service is currently unavailable.");
    }

    public ApiResponse<AircraftResponse> aircraftFallback(Long aircraftId, Throwable ex) {
        return ApiResponse.failure("Aircraft service is currently unavailable.");
    }

    public ApiResponse<Boolean> ownershipFallback(Long aircraftId, Long airlineId, Throwable ex) {
        return ApiResponse.failure("Unable to verify aircraft ownership.");
    }
}
