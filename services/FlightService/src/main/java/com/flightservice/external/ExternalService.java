package com.flightservice.external;

import com.airlineportal.payload.response.Airlines.Aircraft.AircraftResponse;
import com.airlineportal.payload.response.Airlines.Airline.AirlineResponse;
import com.airlineportal.payload.response.ApiResponse;
import com.airlineportal.payload.response.Location.Airport.AirportResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface ExternalService {

    @GetMapping("/internal/airlines/{airlineId}")
    ApiResponse<AirlineResponse> getAirlineById(@PathVariable Long airlineId);

    @GetMapping("/internal/aircrafts/{aircraftId}")
    ApiResponse<AircraftResponse> getAircraftById(@PathVariable Long aircraftId);

    @GetMapping("/internal/airports/{airportId}")
    ApiResponse<AirportResponse> getAirportById(@PathVariable Long airportId);

    @GetMapping("/internal/aircrafts/{aircraftId}/airline/{airlineId}")
    ApiResponse<Boolean> aircraftBelongsToAirline(@PathVariable Long aircraftId, @PathVariable Long airlineId);




}
