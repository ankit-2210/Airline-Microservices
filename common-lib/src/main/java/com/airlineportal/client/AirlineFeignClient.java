package com.airlineportal.client;


import com.airlineportal.payload.response.Airlines.Aircraft.AircraftResponse;
import com.airlineportal.payload.response.Airlines.Airline.AirlineResponse;
import com.airlineportal.payload.response.ApiResponse;
import com.airlineportal.utils.Airline.AirlineStatus;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "AIRLINESERVICE",
        path = "/internal"
)
public interface AirlineFeignClient {

    @GetMapping("/airlines/{airlineId}")
    ApiResponse<AirlineResponse> getAirlineById(@PathVariable Long airlineId);

    @PatchMapping("/{airlineId}/status")
    ApiResponse<AirlineResponse> changeStatus(@PathVariable Long airlineId, @RequestParam AirlineStatus airlineStatus);


    @GetMapping("/aircrafts/{aircraftId}")
    ApiResponse<AircraftResponse> getAircraftById(@PathVariable Long aircraftId);

    @GetMapping("/aircrafts/{aircraftId}/airline/{airlineId}")
    ApiResponse<Boolean> aircraftBelongsToAirline(@PathVariable Long aircraftId, @PathVariable Long airlineId);




}
