package com.airlineportal.client;


import com.airlineportal.payload.response.Airlines.Aircraft.AircraftResponse;
import com.airlineportal.payload.response.Airlines.Airline.AirlineResponse;
import com.airlineportal.payload.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "AIRLINESERVICE",
        path = "/internal"
)
public interface AirlineFeignClient {

    @GetMapping("/airlines/{airlineId}")
    ApiResponse<AirlineResponse> getAirlineById(@PathVariable Long airlineId);

    @GetMapping("/aircrafts/{aircraftId}")
    ApiResponse<AircraftResponse> getAircraftById(@PathVariable Long aircraftId);

    @GetMapping("/aircrafts/{aircraftId}/airline/{airlineId}")
    ApiResponse<Boolean> aircraftBelongsToAirline(@PathVariable Long aircraftId, @PathVariable Long airlineId);




}
