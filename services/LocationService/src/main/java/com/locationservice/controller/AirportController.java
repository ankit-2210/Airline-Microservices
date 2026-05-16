package com.locationservice.controller;

import com.locationservice.service.AirportService;
import com.microservices.payload.request.Airport.AirportRequest;
import com.microservices.payload.response.Airport.AirportResponse;
import com.microservices.payload.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/airports")
public class AirportController {
    private final AirportService airportService;

    @PostMapping
    public ResponseEntity<ApiResponse<AirportResponse>> createAirport(@Valid @RequestBody AirportRequest airportRequest){
        AirportResponse airport = airportService.createAirport(airportRequest);
        ApiResponse<AirportResponse> response = new ApiResponse<>(true, "Airport created successfully", airport);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AirportResponse>> getAirportById(@PathVariable Long id){
        AirportResponse airport = airportService.getAirportById(id);
        ApiResponse<AirportResponse> response = new ApiResponse<>(true, "Airport fetched successfully", airport);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AirportResponse>>> getAllAirports(){
        List<AirportResponse> airports = airportService.getAllAirports();
        ApiResponse<List<AirportResponse>> response = new ApiResponse<>(true, "All airports fetched successfully", airports);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/city/{cityId}")
    public ResponseEntity<ApiResponse<List<AirportResponse>>> getAirportByCityId(@PathVariable Long cityId){
        List<AirportResponse> airports = airportService.getAirportByCityId(cityId);
        ApiResponse<List<AirportResponse>> response = new ApiResponse<>(true, "Airports fetched by city successfully", airports);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AirportResponse>> updateAirport(@PathVariable Long id, @Valid @RequestBody AirportRequest airportRequest){
        AirportResponse updatedAirport = airportService.updateAirport(id, airportRequest);
        ApiResponse<AirportResponse> response = new ApiResponse<>(true, "Airport updated successfully", updatedAirport);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAirport(@PathVariable Long id){
        airportService.deleteAirport(id);
        ApiResponse<Void> response = new ApiResponse<>(true, "Airport deleted successfully", null);
        return ResponseEntity.ok(response);
    }

}
