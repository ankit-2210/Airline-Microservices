package com.flightservice.internal;

import com.airlineportal.payload.response.ApiResponse;
import com.airlineportal.payload.response.Flight.FlightResponse;
import com.flightservice.service.FlightService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/flights")
public class InternalFlightController {
    private final FlightService flightService;


    @GetMapping("/{flightId}")
    public ApiResponse<FlightResponse> getFlightById(@PathVariable Long flightId){
        return ApiResponse.success(flightService.getById(flightId));
    }

    @GetMapping("/{flightId}/airline/{airlineId}")
    public ApiResponse<FlightResponse> getFlightByAirline(@PathVariable Long flightId, @PathVariable Long airlineId){
        return ApiResponse.success(flightService.getByAirlineAndId(airlineId, flightId));
    }

}
