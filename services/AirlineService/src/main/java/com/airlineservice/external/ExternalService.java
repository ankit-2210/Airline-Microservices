package com.airlineservice.external;

import com.airlineportal.payload.response.ApiResponse;
import com.airlineportal.payload.response.Location.Airport.AirportResponse;
import com.airlineportal.payload.response.Location.City.CityResponse;
import com.airlineportal.payload.response.User.UserResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface ExternalService {
    // User Service
    @GetMapping("/internal/users/{userId}")
    ApiResponse<UserResponse> getUserById(@PathVariable Long userId);

    // Location Service
    @GetMapping("/internal/cities/{cityId}")
    ApiResponse<CityResponse> getCityById(Long cityId);

    @GetMapping("/internal//airports/{airportId}")
    ApiResponse<AirportResponse> getAirportById(Long airportId);


}
