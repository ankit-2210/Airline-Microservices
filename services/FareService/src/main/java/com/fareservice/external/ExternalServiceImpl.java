package com.fareservice.external;


import com.airlineportal.client.FlightFeignClient;
import com.airlineportal.payload.response.ApiResponse;
import com.airlineportal.payload.response.Flight.FlightInstanceResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExternalServiceImpl implements ExternalService{
    private final FlightFeignClient flightFeignClient;

    // Flight Instance
    @Override
    @Retry(name = "flightRetry", fallbackMethod = "flightInstanceFallback")
    @CircuitBreaker(name = "flightCB", fallbackMethod = "flightInstanceFallback")
    public ApiResponse<FlightInstanceResponse> getInstanceById(Long instanceId) {
        return flightFeignClient.getInstanceById(instanceId);
    }


    public ApiResponse<FlightInstanceResponse> flightInstanceFallback(Long instanceId, Throwable throwable){
        return ApiResponse.failure("Flight Service is currently unavailable.");
    }




}
