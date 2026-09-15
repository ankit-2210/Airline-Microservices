package com.locationservice.controller;

import com.airlineportal.payload.request.Location.City.CityRequest;
import com.airlineportal.payload.response.ApiResponse;
import com.airlineportal.payload.response.Location.City.CityResponse;
import com.locationservice.service.CityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cities")
public class CityController {
    private final CityService cityService;

    // CREATE CITY
    @PostMapping
    public ApiResponse<CityResponse> createCity(@Valid @RequestBody CityRequest cityRequest){
        return ApiResponse.success(cityService.createCity(cityRequest));
    }

    // GET CITY BY ID
    @GetMapping("/{id}")
    public ApiResponse<CityResponse> getCityById(@PathVariable Long id){
        return ApiResponse.success(cityService.getCityById(id));
    }

    // GET CITY BY CODE
    @GetMapping("/code/{cityCode}")
    public ApiResponse<CityResponse> getCityByCode(@PathVariable String cityCode){
        return ApiResponse.success(cityService.getCityByCode(cityCode));
    }

    // GET ALL CITIES
    @GetMapping
    public ApiResponse<Page<CityResponse>> getAllCities(@PageableDefault(size = 20, sort = "name", direction = Sort.Direction.ASC) Pageable pageable){
        return ApiResponse.success(cityService.getAllCities(pageable));
    }

    // SEARCH CITIES
    @GetMapping("/search")
    public ApiResponse<Page<CityResponse>> searchCities(@RequestParam String keyword, @PageableDefault(size = 20, sort = "name", direction = Sort.Direction.ASC) Pageable pageable){
        return ApiResponse.success(cityService.searchCities(keyword, pageable));
    }

    // GET BY COUNTRY
    @GetMapping("/country/{countryCode}")
    public ApiResponse<Page<CityResponse>> getCitiesByCountryCode(@PathVariable String countryCode, @PageableDefault(size = 20, sort = "name", direction = Sort.Direction.ASC) Pageable pageable){
        return ApiResponse.success(cityService.getCitiesByCountryCode(countryCode, pageable));
    }

    // =========================
    // CITY DROPDOWN
    @GetMapping("/dropdown")
    public ApiResponse<List<CityResponse>> getCityDropdown() {
        return ApiResponse.success(cityService.getCityDropdown());
    }

    // CHECK CITY EXISTS
    @GetMapping("/exists/{cityCode}")
    public ApiResponse<Boolean> cityExists(@PathVariable String cityCode){
        return ApiResponse.success(cityService.cityExists(cityCode));
    }

    // UPDATE CITY
    @PutMapping("/{id}")
    public ApiResponse<CityResponse> updateCity(@PathVariable Long id, @Valid @RequestBody CityRequest cityRequest){
        return ApiResponse.success(cityService.updateCity(id, cityRequest));
    }

    // DELETE CITY
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCity(@PathVariable Long id){
        cityService.deleteCity(id);
        return ResponseEntity.noContent().build();
    }


}
