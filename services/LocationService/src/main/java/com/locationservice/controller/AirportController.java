package com.locationservice.controller;

import com.locationservice.service.AirportService;
import com.microservices.payload.request.Location.Airport.AirportRequest;
import com.microservices.payload.response.Location.Airport.AirportResponse;
import com.microservices.payload.response.ApiResponse;
import jakarta.validation.Valid;
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
@RequestMapping("/api/airports")
public class AirportController {
    private final AirportService airportService;

    // create airport
    @PostMapping
    public ResponseEntity<ApiResponse<AirportResponse>> createAirport(@Valid @RequestBody AirportRequest airportRequest){
        AirportResponse airport = airportService.createAirport(airportRequest);
        ApiResponse<AirportResponse> response = new ApiResponse<>(true, "Airport created successfully", airport);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // get airport by id
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AirportResponse>> getAirportById(@PathVariable Long id){
        AirportResponse airport = airportService.getAirportById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Airport fetched successfully", airport));
    }

    // get airport by iatacode
    @GetMapping("/iata/{iataCode}")
    public ResponseEntity<ApiResponse<AirportResponse>> getAirportByIataCode(@PathVariable String iataCode){
        AirportResponse airport = airportService.getAirportByIataCode(iataCode);
        return ResponseEntity.ok(new ApiResponse<>(true, "Airport fetched successfully", airport));
    }

    // get all airports
    @GetMapping
    public ResponseEntity<ApiResponse<Page<AirportResponse>>> getAllAirports(@RequestParam(defaultValue = "0") int page,
                                                                             @RequestParam(defaultValue = "20") int size,
                                                                             @RequestParam(defaultValue = "name") String sortBy,
                                                                             @RequestParam(defaultValue = "asc") String direction){
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<AirportResponse> airports = airportService.getAllAirports(pageable);
        return ResponseEntity.ok(new ApiResponse<>(true, "All airports fetched successfully", airports));
    }

    // search airports
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<AirportResponse>>> searchAirports(@RequestParam String keyword, @RequestParam(defaultValue = "0") int page,
                                                                             @RequestParam(defaultValue = "20") int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<AirportResponse> airport = airportService.searchAirports(keyword, pageable);
        return ResponseEntity.ok(new ApiResponse<>(true, "Search completed successfully", airport));
    }

    // get airports by city id
    @GetMapping("/city/{cityId}")
    public ResponseEntity<ApiResponse<Page<AirportResponse>>> getAirportByCityId(@PathVariable Long cityId, @RequestParam(defaultValue = "0") int page,
                                                                                                            @RequestParam(defaultValue = "20") int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<AirportResponse> airports = airportService.getAirportByCityId(cityId, pageable);
        return ResponseEntity.ok(new ApiResponse<>(true, "Airports fetched by city successfully", airports));
    }

    // update airport
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AirportResponse>> updateAirport(@PathVariable Long id, @Valid @RequestBody AirportRequest airportRequest){
        AirportResponse updatedAirport = airportService.updateAirport(id, airportRequest);
        return ResponseEntity.ok(new ApiResponse<>(true, "Airport updated successfully", updatedAirport));
    }

    // delete airport bu id
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAirportById(@PathVariable Long id){
        airportService.deleteAirport(id);
        ApiResponse<Void> response = new ApiResponse<>(true, "Airport deleted successfully", null);
        return ResponseEntity.ok(response);
    }

}
