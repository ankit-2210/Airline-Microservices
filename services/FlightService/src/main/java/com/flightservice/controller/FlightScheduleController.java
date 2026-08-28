package com.flightservice.controller;

import com.airlineportal.payload.request.Flight.FlightScheduleRequest;
import com.airlineportal.payload.response.ApiResponse;
import com.airlineportal.payload.response.Flight.FlightScheduleResponse;
import com.flightservice.service.FlightScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.*;

@RestController
@RequestMapping("/api/flights-schedules")
@RequiredArgsConstructor
public class FlightScheduleController {
    private final FlightScheduleService flightScheduleService;

    // Airline creates schedule for a flight
    @PostMapping("/flight/{flightId}/airline/{airlineId}")
    public ApiResponse<FlightScheduleResponse> createSchedule(@PathVariable Long flightId, @PathVariable Long airlineId, @Valid @RequestBody FlightScheduleRequest request){
        return ApiResponse.success(flightScheduleService.createSchedule(flightId, request, airlineId));
    }

    // Get schedule by ID
    @GetMapping("/{scheduleId}")
    public ApiResponse<FlightScheduleResponse> getById(@PathVariable Long scheduleId){
        return ApiResponse.success(flightScheduleService.getById(scheduleId));
    }

    // Get schedules of a flight
    @GetMapping("/flight/{flightId}")
    public ApiResponse<Page<FlightScheduleResponse>> getByFlight(@PathVariable Long flightId, Pageable pageable){
        return ApiResponse.success(flightScheduleService.getByFlight(flightId, pageable));
    }

    // Get schedules of an airline
    @GetMapping("/airline/{airlineId}")
    public ApiResponse<Page<FlightScheduleResponse>> getByAirline(@PathVariable Long airlineId, Pageable pageable){
        return ApiResponse.success(flightScheduleService.getByAirline(airlineId, pageable));
    }

    // Get schedules by route
    @GetMapping("/route")
    public ApiResponse<Page<FlightScheduleResponse>> getByRoute(@RequestParam Long departureAirportId, @RequestParam Long arrivalAirportId, Pageable pageable){
        return ApiResponse.success(flightScheduleService.getByRoute(departureAirportId, arrivalAirportId, pageable));
    }

    // Get schedules operating on a particular day
    @GetMapping("/operating-day/{day}")
    public ApiResponse<List<FlightScheduleResponse>> getByOperatingDay(@PathVariable DayOfWeek day){
        return ApiResponse.success(flightScheduleService.getByOperatingDay(day));
    }

    // Airline updates schedule
    @PutMapping("/{scheduleId}/airline/{airlineId}")
    public ApiResponse<FlightScheduleResponse> updateSchedule(@PathVariable Long scheduleId, @PathVariable Long airlineId, @Valid @RequestBody FlightScheduleRequest request){
        return ApiResponse.success(flightScheduleService.updateSchedule(scheduleId, request, airlineId));
    }


    // Airline deletes schedule
    @DeleteMapping("/{scheduleId}/airline/{airlineId}")
    public void deleteSchedule(@PathVariable Long scheduleId, @PathVariable Long airlineId){
        flightScheduleService.deleteSchedule(scheduleId, airlineId);
    }

}
