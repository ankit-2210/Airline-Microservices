package com.airlineservice.controller;

import com.airlineservice.service.AircraftService;
import com.microservices.payload.request.Airlines.Aircraft.AircraftRequest;
import com.microservices.payload.response.Airline.AircraftResponse;
import com.microservices.payload.response.Airline.AirlineResponse;
import com.microservices.payload.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/aircrafts")
public class AircraftController {
    private final AircraftService aircraftService;

    // create aircraft
    @PostMapping
    public ResponseEntity<ApiResponse<AircraftResponse>> createAircraft(@Valid @RequestBody AircraftRequest aircraftRequest, @RequestParam Long ownerId){
        AircraftResponse aircraftResponse = aircraftService.createAircraft(aircraftRequest, ownerId);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(true, "Aircraft created successfully", aircraftResponse));
    }

    // get aircraft by id
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AircraftResponse>> getAircraftById(@PathVariable Long id){
        AircraftResponse aircraftResponse = aircraftService.getById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Aircraft fetched successfully", aircraftResponse));
    }

    // get aircraft by owner
    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<ApiResponse<Page<AircraftResponse>>> getAircraftByOwner(@PathVariable Long ownerId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size,
                                                                                  @RequestParam(defaultValue = "code") String sortBy, @RequestParam(defaultValue = "asc") String direction) {
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<AircraftResponse> aircraft = aircraftService.allAircraftByOwner(ownerId, pageable);
        return ResponseEntity.ok(new ApiResponse<>(true, "Aircraft fetched successfully", aircraft));
    }

    // update aircraft
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AircraftResponse>> updateAircraft(@PathVariable Long id, @Valid @RequestBody AircraftRequest aircraftRequest, @RequestParam Long ownerId) {
        AircraftResponse aircraft = aircraftService.updateAircraft(id, aircraftRequest, ownerId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Aircraft updated successfully", aircraft));
    }

    // delete aircraft
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAircraft(@PathVariable Long id, @RequestParam Long ownerId) {
        aircraftService.deleteAircraft(id, ownerId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Aircraft deleted successfully", null));
    }


}
