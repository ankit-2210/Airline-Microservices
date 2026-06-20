package com.flightservice.controller;

import com.flightservice.service.FlightInstanceService;
import com.microservices.payload.request.Flight.FlightInstanceRequest;
import com.microservices.payload.response.ApiResponse;
import com.microservices.payload.response.Flight.FlightInstanceResponse;
import com.microservices.utils.Flight.FlightStatus;
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


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/flights-instances")
public class FlightInstanceController {
    private final FlightInstanceService flightInstanceService;

    // create
    @PostMapping
    public ResponseEntity<ApiResponse<FlightInstanceResponse>> createFlightInstance(@RequestParam Long airlineId, @Valid @RequestBody FlightInstanceRequest request){
        FlightInstanceResponse response = flightInstanceService.createFlightInstance(airlineId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(true, "Flight instance created successfully", response));
    }

    // get by id
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<FlightInstanceResponse>> getById(@PathVariable Long id) {
        FlightInstanceResponse response = flightInstanceService.getFlightInstanceById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Flight instance fetched successfully", response));
    }

    // Get by airline
    @GetMapping
    public ResponseEntity<ApiResponse<Page<FlightInstanceResponse>>> getByAirline(@RequestParam Long airlineId, @RequestParam(required = false) Long departureAirportId,
                                                                                  @RequestParam(required = false) Long arrivalAirportId, @RequestParam(required = false) Long flightId,
                                                                                  @RequestParam(required = false) LocalDate onDate,
                                                                                  @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size,
                                                                                  @RequestParam(defaultValue = "departureDateTime") String sortBy, @RequestParam(defaultValue = "asc") String direction) {
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<FlightInstanceResponse> response = flightInstanceService.getByAirlineId(airlineId, departureAirportId, arrivalAirportId, flightId, onDate, pageable);
        return ResponseEntity.ok(new ApiResponse<>(true, "Flight instances fetched successfully", response));
    }

    // get by flight
    @GetMapping("/flight/{flightId}")
    public ResponseEntity<ApiResponse<Page<FlightInstanceResponse>>> getByFlight(@PathVariable Long flightId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<FlightInstanceResponse> response = flightInstanceService.getByFlightId(flightId, pageable);
        return ResponseEntity.ok(new ApiResponse<>(true, "Flight instances fetched successfully", response));
    }

    // update
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<FlightInstanceResponse>> updateFlightInstance(@RequestParam Long airlineId, @PathVariable Long id, @Valid @RequestBody FlightInstanceRequest request) {
        FlightInstanceResponse response = flightInstanceService.updateFlightInstance(airlineId, id, request);
        return ResponseEntity.ok(new ApiResponse<>(true, "Flight instance updated successfully", response));
    }

    // change status
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<FlightInstanceResponse>> changeStatus(@RequestParam Long airlineId, @PathVariable Long id, @RequestParam FlightStatus flightStatus) {
        FlightInstanceResponse response = flightInstanceService.changeStatus(airlineId, id, flightStatus);
        return ResponseEntity.ok(new ApiResponse<>(true, "Flight status updated successfully", response));
    }

    // update available seats
    @PatchMapping("/{id}/available-seats")
    public ResponseEntity<ApiResponse<FlightInstanceResponse>> updateAvailableSeats(@RequestParam Long airlineId, @PathVariable Long id, @RequestParam Integer availableSeats) {
        FlightInstanceResponse response = flightInstanceService.updateAvailableSeats(airlineId, id, availableSeats);
        return ResponseEntity.ok(new ApiResponse<>(true, "Flight seats updated successfully", response));
    }

    // toggle active
    @PatchMapping("/{id}/active")
    public ResponseEntity<ApiResponse<FlightInstanceResponse>> toggleActive(@RequestParam Long airlineId, @PathVariable Long id, @RequestParam Boolean active) {
        FlightInstanceResponse response = flightInstanceService.toggleActive(airlineId, id, active);
        return ResponseEntity.ok(new ApiResponse<>(true, "Flight instance status updated successfully", response));
    }

    // delete
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteFlightInstance(@RequestParam Long airlineId, @PathVariable Long id) {
        flightInstanceService.deleteFlightInstance(airlineId, id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Flight instance deleted successfully", null));
    }

}
