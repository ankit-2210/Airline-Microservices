package com.locationservice.controller;

import com.airlineportal.payload.request.Location.Airport.AirportRequest;
import com.airlineportal.payload.response.ApiResponse;
import com.airlineportal.payload.response.Location.Airport.AirportResponse;
import com.locationservice.service.AirportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/airports")
public class AirportController {
    private final AirportService airportService;

    // create airport
    @PostMapping
    public ApiResponse<AirportResponse> createAirport(@Valid @RequestBody AirportRequest airportRequest){
        return ApiResponse.success(airportService.createAirport(airportRequest));
    }

    // GET ALL AIRPORTS
    @GetMapping
    public ApiResponse<Page<AirportResponse>> getAllAirports(@PageableDefault(size = 20, sort = "name", direction = Sort.Direction.ASC) Pageable pageable){
        return ApiResponse.success(airportService.getAllAirports(pageable));
    }

    // GET AIRPORT BY ID
    @GetMapping("/{id}")
    public ApiResponse<AirportResponse> getAirportById(@PathVariable Long id){
        return ApiResponse.success(airportService.getAirportById(id));
    }

    // GET AIRPORT BY IATA CODE
    @GetMapping("/iata/{iataCode}")
    public ApiResponse<AirportResponse> getAirportByIataCode(@PathVariable String iataCode){
        return ApiResponse.success(airportService.getAirportByIataCode(iataCode));
    }

    // SEARCH AIRPORTS
    @GetMapping("/search")
    public ApiResponse<Page<AirportResponse>> searchAirports(@RequestParam String keyword, @PageableDefault(size = 20, sort = "name", direction = Sort.Direction.ASC) Pageable pageable){
        return ApiResponse.success(airportService.searchAirports(keyword, pageable));
    }

    // GET AIRPORTS BY CITY
    @GetMapping("/city/{cityId}")
    public ApiResponse<Page<AirportResponse>> getAirportsByCity(@PathVariable Long cityId, @PageableDefault(size = 20, sort = "name", direction = Sort.Direction.ASC) Pageable pageable){
        return ApiResponse.success(airportService.getAirportsByCity(cityId, pageable));
    }

    // GET AIRPORTS BY COUNTRY
    @GetMapping("/country/{countryCode}")
    public ApiResponse<Page<AirportResponse>> getAirportsByCountry(@PathVariable String countryCode, @PageableDefault(size = 20, sort = "name", direction = Sort.Direction.ASC) Pageable pageable){
        return ApiResponse.success(airportService.getAirportsByCountry(countryCode, pageable));
    }

    // AIRPORT DROPDOWN
    @GetMapping("/dropdown")
    public ApiResponse<List<AirportResponse>> getAirportDropdown(){
        return ApiResponse.success(airportService.getAirportDropdown());
    }

    // CHECK AIRPORT EXISTS
    @GetMapping("/exists/{iataCode}")
    public ApiResponse<Boolean> airportExists(@PathVariable String iataCode){
        return ApiResponse.success(airportService.airportExists(iataCode));
    }

    // UPDATE AIRPORT
    @PutMapping("/{id}")
    public ApiResponse<AirportResponse> updateAirport(@PathVariable Long id, @Valid @RequestBody AirportRequest airportRequest){
        return ApiResponse.success(airportService.updateAirport(id, airportRequest));
    }

    // DELETE AIRPORT
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAirport(@PathVariable Long id){
        airportService.deleteAirport(id);
        return ResponseEntity.noContent().build();
    }


}
