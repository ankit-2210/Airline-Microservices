package com.flightservice.internal;

import com.airlineportal.payload.response.ApiResponse;
import com.airlineportal.payload.response.Flight.FlightScheduleResponse;
import com.flightservice.service.FlightScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/flight-schedules")
public class InternalFlightScheduleController {
    private final FlightScheduleService flightScheduleService;

    @GetMapping("/{scheduleId}")
    public ApiResponse<FlightScheduleResponse> getScheduleById(@PathVariable Long scheduleId){
        return ApiResponse.success(flightScheduleService.getById(scheduleId));
    }


}
