package com.airlineportal.client;


import com.airlineportal.payload.response.ApiResponse;
import com.airlineportal.payload.response.Flight.FlightInstanceResponse;
import com.airlineportal.payload.response.Flight.FlightResponse;
import com.airlineportal.payload.response.Flight.FlightScheduleResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "FLIGHTSERVICE",
        path = "/internal"
)
public interface FlightFeignClient {

    @GetMapping("/flights/{flightId}")
    ApiResponse<FlightResponse> getFlightById(@PathVariable Long flightId);

    @GetMapping("/flights/{flightId}/airline/{airlineId}")
    ApiResponse<FlightResponse> getFlightByAirline(@PathVariable Long flightId, @PathVariable Long airlineId);



    @GetMapping("/flight-instances/{instanceId}")
    ApiResponse<FlightInstanceResponse> getInstanceById(@PathVariable Long instanceId);

    @PostMapping("/flight-instances/{instanceId}/reserve")
    ApiResponse<Boolean> reserveSeats(@PathVariable("instanceId") Long instanceId, @RequestParam("seats") Integer seats);

    @PostMapping("/flight-instances/{instanceId}/release")
    ApiResponse<Boolean> releaseSeats(@PathVariable("instanceId") Long instanceId, @RequestParam("seats") Integer seats);



    @GetMapping("/flight-schedules/{scheduleId}")
    ApiResponse<FlightScheduleResponse> getScheduleById(@PathVariable Long scheduleId);


}
