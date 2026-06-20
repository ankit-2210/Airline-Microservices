package com.flightservice.controller;

import com.flightservice.model.Flight;
import com.flightservice.repository.FlightScheduleRepository;
import com.flightservice.service.FlightScheduleService;
import com.microservices.payload.request.Flight.FlightScheduleRequest;
import com.microservices.payload.response.ApiResponse;
import com.microservices.payload.response.Flight.FlightInstanceResponse;
import com.microservices.payload.response.Flight.FlightScheduleResponse;
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
@RequiredArgsConstructor
@RequestMapping("/api/flights-schedules")
public class FlightScheduleController {
    private final FlightScheduleService flightScheduleService;

    @PostMapping
    public ResponseEntity<ApiResponse<FlightScheduleResponse>> create(@RequestParam Long airlineId, @Valid @RequestBody FlightScheduleRequest request){
        FlightScheduleResponse response = flightScheduleService.createFlightSchedule(airlineId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(true, "Schedule created successfully", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<FlightScheduleResponse>> getByScheduleId(@PathVariable Long id){
        FlightScheduleResponse response = flightScheduleService.getFlightScheduleById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Schedule fetched successfully", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<FlightScheduleResponse>>> getByAirline(@RequestParam Long airlineId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<FlightScheduleResponse> response = flightScheduleService.getFlightScheduleByAirline(airlineId, pageable);
        return ResponseEntity.ok(new ApiResponse<>(true, "Schedule fetched successfully", response));
    }

    @GetMapping("/flight/{flightId}")
    public ResponseEntity<ApiResponse<Page<FlightScheduleResponse>>> getByFlight(@PathVariable Long flightId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<FlightScheduleResponse> responses = flightScheduleService.getScheduleByFlightId(flightId, pageable);
        return ResponseEntity.ok(new ApiResponse<>(true, "Schedules fetched", responses));
    }


    @GetMapping("/route")
    public ResponseEntity<ApiResponse<Page<FlightScheduleResponse>>> route(@RequestParam Long departureAirportId, @RequestParam Long arrivalAirportId,
                                                                           @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<FlightScheduleResponse> responses = flightScheduleService.getScheduleByRoute(departureAirportId, arrivalAirportId, pageable);
        return ResponseEntity.ok(new ApiResponse<>(true, "Route schedules fetched", responses));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<FlightScheduleResponse>> update(@RequestParam Long airlineId, @PathVariable Long id, @Valid @RequestBody FlightScheduleRequest request){
        FlightScheduleResponse response = flightScheduleService.updateFlightSchedule(airlineId, id, request);
        return ResponseEntity.ok(new ApiResponse<>(true, "Schedule updated successfully", response));
    }

    @PatchMapping("/{id}/active")
    public ResponseEntity<ApiResponse<FlightScheduleResponse>> active(@RequestParam Long airlineId, @PathVariable Long id, @RequestParam Boolean active){
        FlightScheduleResponse response = flightScheduleService.changeActiveStatus(airlineId, id, active);
        return ResponseEntity.ok(new ApiResponse<>(true, "Schedule status changed", response));
    }

    @GetMapping("/day/{day}")
    public ResponseEntity<ApiResponse<List<FlightScheduleResponse>>> day(@PathVariable DayOfWeek day){
        List<FlightScheduleResponse> response = flightScheduleService.getScheduleByOperatingDay(day);
        return ResponseEntity.ok(new ApiResponse<>(true, "Schedule fetched", response));
    }

    @PostMapping("/{scheduleId}/generate-instances")
    public ResponseEntity<ApiResponse<Void>> generateFlightInstances(@PathVariable Long scheduleId){
        flightScheduleService.generateFlightInstances(scheduleId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Flight instances generated successfully", null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@RequestParam Long airlineId, @PathVariable Long id){
        flightScheduleService.deleteFlightSchedule(airlineId, id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Schedule deleted", null));
    }



}
