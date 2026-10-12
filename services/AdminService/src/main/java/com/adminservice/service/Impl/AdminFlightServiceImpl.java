package com.adminservice.service.Impl;

import com.adminservice.service.AdminFlightService;
import com.airlineportal.client.FlightFeignClient;
import com.airlineportal.payload.response.ApiResponse;
import com.airlineportal.payload.response.Flight.FlightInstanceResponse;
import com.airlineportal.payload.response.Flight.FlightResponse;
import com.airlineportal.payload.response.Flight.FlightScheduleResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminFlightServiceImpl implements AdminFlightService {
    private final FlightFeignClient flightFeignClient;

    @Override
    @Retry(name = "flightRetry", fallbackMethod = "flightFallback")
    @CircuitBreaker(name = "flightCB", fallbackMethod = "flightFallback")
    public ApiResponse<FlightResponse> getFlightById(Long flightId) {
        return flightFeignClient.getFlightById(flightId);
    }

    @Override
    @Retry(name = "flightRetry", fallbackMethod = "scheduleFallback")
    @CircuitBreaker(name = "flightCB", fallbackMethod = "scheduleFallback")
    public ApiResponse<FlightScheduleResponse> getScheduleById(Long scheduleId) {
        return flightFeignClient.getScheduleById(scheduleId);
    }

    @Override
    @Retry(name = "flightRetry", fallbackMethod = "instanceFallback")
    @CircuitBreaker(name = "flightCB", fallbackMethod = "instanceFallback")
    public ApiResponse<FlightInstanceResponse> getInstanceById(Long instanceId) {
        return flightFeignClient.getInstanceById(instanceId);
    }



    public ApiResponse<FlightResponse> flightFallback(Long flightId, Throwable ex) {
        return ApiResponse.failure("Flight service is currently unavailable.");
    }

    public ApiResponse<FlightScheduleResponse> scheduleFallback(Long scheduleId, Throwable ex) {
        return ApiResponse.failure("Flight schedule service is unavailable.");
    }

    public ApiResponse<FlightInstanceResponse> instanceFallback(Long instanceId, Throwable ex) {
        return ApiResponse.failure("Flight instance service is unavailable.");
    }


}
