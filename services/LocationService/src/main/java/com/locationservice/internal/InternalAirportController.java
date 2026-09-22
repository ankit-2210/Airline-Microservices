package com.locationservice.internal;


import com.airlineportal.payload.response.Location.Airport.AirportResponse;
import com.locationservice.service.AirportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/airports")
public class InternalAirportController {
    private final AirportService airportService;

    @GetMapping("/{airportId}")
    public AirportResponse getAirportById(@PathVariable Long airportId){
        return airportService.getAirportById(airportId);
    }

    @GetMapping("/iata/{iataCode}")
    public AirportResponse getAirportByIataCode(@PathVariable String iataCode){
        return airportService.getAirportByIataCode(iataCode);
    }

}
