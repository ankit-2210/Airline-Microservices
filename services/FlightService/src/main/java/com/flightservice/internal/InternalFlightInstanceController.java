package com.flightservice.internal;

import com.airlineportal.payload.response.ApiResponse;
import com.airlineportal.payload.response.Flight.FlightInstanceResponse;
import com.flightservice.service.FlightInstanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/flight-instances")
public class InternalFlightInstanceController {
    private final FlightInstanceService flightInstanceService;

    @GetMapping("/{instanceId}")
    public ApiResponse<FlightInstanceResponse> getInstanceById(@PathVariable Long instanceId){
        return ApiResponse.success(flightInstanceService.getById(instanceId));
    }

    @PostMapping("/{instanceId}/reserve")
    public ApiResponse<Boolean> reserveSeats(@PathVariable("instanceId") Long instanceId, @RequestParam("seats") Integer seats){
        return ApiResponse.success(flightInstanceService.reserveSeats(instanceId, seats));
    }

    @PostMapping("/{instanceId}/release")
    public ApiResponse<Boolean> releaseSeats(@PathVariable("instanceId") Long instanceId, @RequestParam("seats") Integer seats){
        return ApiResponse.success(flightInstanceService.releaseSeats(instanceId, seats));
    }

}
