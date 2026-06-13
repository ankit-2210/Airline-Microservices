package com.airlineservice.controller;

import com.airlineservice.service.AirlineService;
import com.microservices.payload.request.Airline.AirlineRequest;
import com.microservices.payload.response.Airline.AirlineDropdownItem;
import com.microservices.payload.response.Airline.AirlineResponse;
import com.microservices.payload.response.ApiResponse;
import com.microservices.utils.Airport.AirlineStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/airlines")
public class AirlineController {
    private final AirlineService airlineService;

    // create airline
    @PostMapping
    public ResponseEntity<ApiResponse<AirlineResponse>> createAirline(@Valid @RequestBody AirlineRequest airlineRequest, @RequestParam Long ownerId){
        AirlineResponse airlineResponse = airlineService.createAirline(airlineRequest, ownerId);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(true, "Airport created successfully", airlineResponse));
    }

    // get airline by id
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AirlineResponse>> getAirlineById(@PathVariable Long id){
        AirlineResponse airlineResponse = airlineService.getAirlineById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Airline fetched successfully", airlineResponse));
    }

    // get airline by owner
    public ResponseEntity<ApiResponse<AirlineResponse>> getAirportByOwner(@PathVariable Long ownerId) {
        AirlineResponse airlineResponse = airlineService.getAirlineByOwner(ownerId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Airline fetched successfully", airlineResponse));
    }

    // get all airlines
    public ResponseEntity<ApiResponse<Page<AirlineResponse>>> getAllAirlines(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size,
                                                                                @RequestParam(defaultValue = "name") String sortBy, @RequestParam(defaultValue = "asc") String direction) {
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<AirlineResponse> airlines = airlineService.getAllAirlines(pageable);
        return ResponseEntity.ok(new ApiResponse<>(true, "All airlines fetched successfully", airlines));
    }

    // update airline
    public ResponseEntity<ApiResponse<AirlineResponse>> updateAirline(@PathVariable Long airlineId, @Valid @RequestBody AirlineRequest airlineRequest, @RequestParam Long ownerId){
        AirlineResponse airlineResponse = airlineService.updateAirline(airlineId, airlineRequest, ownerId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Airline updated successfully", airlineResponse));
    }

    // change status (admin)
    @PatchMapping
    public ResponseEntity<ApiResponse<AirlineResponse>> changeStatus(@PathVariable Long airlineId, @RequestParam AirlineStatus airlineStatus){
        AirlineResponse airlineResponse = airlineService.changeStatusByAdmin(airlineId, airlineStatus);
        return ResponseEntity.ok(new ApiResponse<>(true, "Airline updated successfully", airlineResponse));
    }

    // delete airline
    @DeleteMapping("/{airlineId}")
    public ResponseEntity<ApiResponse<Void>> deleteAirline(@PathVariable Long airlineId, @RequestParam Long ownerId){
        airlineService.deleteAirline(airlineId, ownerId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Airline deleted successfully", null));
    }

    // dropdown
    public ResponseEntity<ApiResponse<List<AirlineDropdownItem>>> getDropdown(){
        List<AirlineDropdownItem> airlines = airlineService.getAirlineDropdown();
        return ResponseEntity.ok(new ApiResponse<>(true, "Airline dropdown fetched successfully", airlines));
    }

}
