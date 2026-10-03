package com.fareservice.external;


import com.airlineportal.payload.response.ApiResponse;
import com.airlineportal.payload.response.Flight.FlightInstanceResponse;
import org.springframework.web.bind.annotation.*;

public interface ExternalService {
    @GetMapping("/internal/flight-instances/{instanceId}")
    ApiResponse<FlightInstanceResponse> getInstanceById(@PathVariable Long instanceId);




}
