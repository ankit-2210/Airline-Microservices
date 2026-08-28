package com.flightservice.controller;

import com.airlineportal.payload.request.Flight.FlightInstanceRequest;
import com.airlineportal.payload.response.ApiResponse;
import com.airlineportal.payload.response.Flight.FlightInstanceResponse;
import com.flightservice.service.FlightInstanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.LocalDateTime;


@RestController
@RequestMapping("/api/flights-instances")
@RequiredArgsConstructor
public class FlightInstanceController {
    private final FlightInstanceService flightInstanceService;

    // Airline creates a concrete flight instance
    @PostMapping("/flight/{flightId}/schedule/{scheduleId}/airline/{airlineId}")
    public ApiResponse<FlightInstanceResponse> createInstance(@PathVariable Long flightId, @PathVariable Long scheduleId, @PathVariable Long airlineId,
            @Valid @RequestBody FlightInstanceRequest request){
        return ApiResponse.success(flightInstanceService.createInstance(flightId, scheduleId, request, airlineId));
    }

    // Get instance by Id
    @GetMapping("/{instanceId}")
    public ApiResponse<FlightInstanceResponse> getById(@PathVariable Long instanceId) {
        return ApiResponse.success(flightInstanceService.getById(instanceId));
    }

    // Get instances of a flight
    @GetMapping("/flight/{flightId}")
    public ApiResponse<Page<FlightInstanceResponse>> getByFlight(@PathVariable Long flightId, Pageable pageable){
        return ApiResponse.success(flightInstanceService.getByFlight(flightId, pageable));
    }

    // Get all instances of an airline
    @GetMapping("/airline/{airlineId}")
    public ApiResponse<Page<FlightInstanceResponse>> getByAirline(@PathVariable Long airlineId, Pageable pageable){
        return ApiResponse.success(flightInstanceService.getByAirline(airlineId, pageable));
    }

    // Search instances
    @GetMapping("/search")
    public ApiResponse<Page<FlightInstanceResponse>> search(@RequestParam Long airlineId,
            @RequestParam(required = false) Long departureAirportId, @RequestParam(required = false) Long arrivalAirportId,
            @RequestParam(required = false) Long flightId,
            @RequestParam(required = false) LocalDateTime dayStart, @RequestParam(required = false) LocalDateTime dayEnd,
            Pageable pageable){
        return ApiResponse.success(flightInstanceService.search(airlineId, departureAirportId, arrivalAirportId, flightId, dayStart, dayEnd, pageable));
    }

    // Airline updates instance
    @PutMapping("/{instanceId}/airline/{airlineId}")
    public ApiResponse<FlightInstanceResponse> updateInstance(@PathVariable Long instanceId, @PathVariable Long airlineId,
            @Valid @RequestBody FlightInstanceRequest request){
        return ApiResponse.success(flightInstanceService.updateInstance(instanceId, request, airlineId));
    }

    // Airline deletes instance
    @DeleteMapping("/{instanceId}/airline/{airlineId}")
    public void deleteInstance(@PathVariable Long instanceId, @PathVariable Long airlineId){
        flightInstanceService.deleteInstance(instanceId, airlineId);
    }


}
