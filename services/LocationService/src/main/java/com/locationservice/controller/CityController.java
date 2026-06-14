package com.locationservice.controller;

import com.locationservice.service.CityService;
import com.microservices.payload.request.Location.City.CityRequest;
import com.microservices.payload.response.ApiResponse;
import com.microservices.payload.response.City.CityResponse;
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
@RequestMapping("/api/cities")
public class CityController {
    private final CityService cityService;

    // create city
    @PostMapping
    public ResponseEntity<ApiResponse<CityResponse>> createCity(@Valid @RequestBody CityRequest cityRequest) throws Exception {
        CityResponse cityResponse = cityService.createCity(cityRequest);
        ApiResponse<CityResponse> response = new ApiResponse<>(true, "City create successfully", cityResponse);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // get city by id
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CityResponse>> getCityById(@PathVariable Long id) throws Exception {
        CityResponse cityResponse = cityService.getCityById(id);
        ApiResponse<CityResponse> response = new ApiResponse<>(true, "City fetched successfully", cityResponse);
        return ResponseEntity.ok(response);
    }

    // get all cities
    @GetMapping
    public ResponseEntity<ApiResponse<Page<CityResponse>>> getAllCities(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy, @RequestParam(defaultValue = "asc") String sortDirection){
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<CityResponse> cities = cityService.getAllCities(pageable);
        ApiResponse<Page<CityResponse>> response = new ApiResponse<>(true, "Cities fetched successfully", cities);
        return ResponseEntity.ok(response);
    }

    // update city by id
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CityResponse>> updateCity(@PathVariable Long id, @Valid @RequestBody CityRequest cityRequest) throws Exception {
        CityResponse cityResponse = cityService.updateCity(id, cityRequest);
        ApiResponse<CityResponse> response = new ApiResponse<>(true, "City updated successfully", cityResponse);
        return ResponseEntity.ok(response);
    }

    // delete city by id
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCityById(@PathVariable Long id) throws Exception {
        cityService.deleteCity(id);
        ApiResponse<Void> response = new ApiResponse<>(true, "City deleted successfully", null);
        return ResponseEntity.ok(response);
    }

    //search cities
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<CityResponse>>> searchCities(@RequestParam String keyword, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CityResponse> result = cityService.searchCities(keyword, pageable);
        ApiResponse<Page<CityResponse>> response = new ApiResponse<>(true, "Search results fetched successfully", result);
        return ResponseEntity.ok(response);
    }

    // get cities by countryCode
    @GetMapping("/country/{countryCode}")
    public ResponseEntity<ApiResponse<Page<CityResponse>>> getCitiesByCountryCode(@PathVariable String countryCode, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CityResponse> result = cityService.getCitiesByCountryCode(countryCode, pageable);
        ApiResponse<Page<CityResponse>> response = new ApiResponse<>(true, "Cities fetched successfully by country code", result);
        return ResponseEntity.ok(response);
    }

    // check city is exist or not
    @GetMapping("/exists/{cityCode}")
    public ResponseEntity<ApiResponse<Boolean>> checkCityExists(@PathVariable String cityCode){
        boolean exists = cityService.cityExists(cityCode);
        ApiResponse<Boolean> response = new ApiResponse<>(true, exists ? "City exists" : "City does not exist", exists);
        return ResponseEntity.ok(response);
    }


}
