package com.locationservice.internal;


import com.airlineportal.payload.response.ApiResponse;
import com.airlineportal.payload.response.Location.Airport.AirportResponse;
import com.locationservice.service.AirportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/airports")
public class InternalAirportController {
    private final AirportService airportService;

    @GetMapping("/{airportId}")
    public ApiResponse<AirportResponse> getAirportById(@PathVariable Long airportId){
        return ApiResponse.success(airportService.getAirportById(airportId));
    }

    @GetMapping("/iata/{iataCode}")
    public ApiResponse<AirportResponse> getAirportByIataCode(@PathVariable String iataCode){
        return ApiResponse.success(airportService.getAirportByIataCode(iataCode));
    }

}
