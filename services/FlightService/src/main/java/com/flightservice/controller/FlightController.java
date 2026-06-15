package com.flightservice.controller;

import com.flightservice.service.FlightService;
import com.microservices.payload.request.Flight.FlightRequest;
import com.microservices.payload.response.ApiResponse;
import com.microservices.payload.response.Flight.FlightResponse;
import com.microservices.utils.Flight.FlightStatus;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/flights")
public class FlightController {
    private final FlightService flightService;

    // create flight
    @PostMapping
    public ResponseEntity<ApiResponse<FlightResponse>> createFlight(@RequestParam Long airlineId, @Valid @RequestBody FlightRequest flightRequest) {
        FlightResponse flight = flightService.createFlight(airlineId, flightRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(true, "Flight created successfully", flight));
    }

    // get flight by id
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<FlightResponse>> getFlightById(@PathVariable Long id){
        FlightResponse flightResponse = flightService.getFlightById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Flight fetched successfully", flightResponse));
    }

    // get flights by airline
    @GetMapping
    public ResponseEntity<ApiResponse<Page<FlightResponse>>> getFlightsByAirline(@RequestParam Long airlineId,
                                                                                 @RequestParam(required = false) Long departureAirportId, @RequestParam(required = false) Long arrivalAirportId,
                                                                                 @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size,
                                                                                 @RequestParam(defaultValue = "scheduledDeparture") String sortBy, @RequestParam(defaultValue = "asc") String direction) {
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<FlightResponse> flights = flightService.getFlightByAirline(airlineId, departureAirportId, arrivalAirportId, pageable);
        return ResponseEntity.ok(new ApiResponse<>(true, "Flights fetched successfully", flights));
    }


    // update flight
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<FlightResponse>> updateFlight(@RequestParam Long airlineId, @PathVariable Long id, @Valid @RequestBody FlightRequest flightRequest) {
        FlightResponse flightResponse = flightService.updateFlight(airlineId, id, flightRequest);
        return ResponseEntity.ok(new ApiResponse<>(true, "Flight updated successfully", flightResponse));
    }

    // change status
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<FlightResponse>> changeStatus(@RequestParam Long airlineId, @PathVariable Long id, @RequestParam FlightStatus flightStatus) {
        FlightResponse flightResponse = flightService.changeStatus(airlineId, id, flightStatus);
        return ResponseEntity.ok(new ApiResponse<>(true, "Flight status updated successfully", flightResponse));
    }

    // delete flight
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteFlight(@RequestParam Long airlineId, @PathVariable Long id){
        flightService.deleteFlight(airlineId, id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Flight deleted successfully", null));
    }

}
