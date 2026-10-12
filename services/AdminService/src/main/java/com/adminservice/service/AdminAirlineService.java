package com.adminservice.service;

import com.airlineportal.payload.response.Airlines.Aircraft.AircraftResponse;
import com.airlineportal.payload.response.Airlines.Airline.AirlineResponse;
import com.airlineportal.payload.response.ApiResponse;
import com.airlineportal.utils.Airline.AirlineStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

public interface AdminAirlineService {
    ApiResponse<AirlineResponse> getAirlineById(Long airlineId);

    ApiResponse<AirlineResponse> changeAirlineStatus(@PathVariable Long airlineId, @RequestParam AirlineStatus airlineStatus);

    ApiResponse<AircraftResponse> getAircraftById(Long aircraftId);

    ApiResponse<Boolean> aircraftBelongsToAirline(Long aircraftId, Long airlineId);


}
