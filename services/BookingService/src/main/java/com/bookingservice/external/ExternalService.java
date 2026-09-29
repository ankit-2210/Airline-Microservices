package com.bookingservice.external;

import com.airlineportal.payload.response.ApiResponse;
import com.airlineportal.payload.response.Flight.FlightInstanceResponse;
import com.airlineportal.payload.response.Flight.FlightResponse;
import com.airlineportal.payload.response.User.UserResponse;
import org.springframework.web.bind.annotation.*;

public interface ExternalService {

    // User Service
    @GetMapping("/internal/users/{userId}")
    ApiResponse<UserResponse> getUserById(@PathVariable Long userId);

    @GetMapping("/internal/flights/{flightId}")
    ApiResponse<FlightResponse> getFlightById(@PathVariable Long flightId);


    @GetMapping("/internal/flight-instances/{instanceId}")
    ApiResponse<FlightInstanceResponse> getInstanceById(@PathVariable Long instanceId);

    @PostMapping("/internal/flight-instances/{instanceId}/reserve")
    ApiResponse<Boolean> reserveSeats(@PathVariable("instanceId") Long instanceId, @RequestParam("seats") Integer seats);

    @PostMapping("/internal/flight-instances/{instanceId}/release")
    ApiResponse<Boolean> releaseSeats(@PathVariable("instanceId") Long instanceId, @RequestParam("seats") Integer seats);




}
