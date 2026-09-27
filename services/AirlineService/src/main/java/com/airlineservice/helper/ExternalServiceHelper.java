package com.airlineservice.helper;

import com.airlineportal.exception.ResourceNotFoundException;
import com.airlineportal.payload.response.ApiResponse;
import com.airlineportal.payload.response.Location.Airport.AirportResponse;
import com.airlineportal.payload.response.Location.City.CityResponse;
import com.airlineportal.payload.response.User.UserResponse;
import com.airlineservice.external.ExternalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExternalServiceHelper {
    private final ExternalService externalService;

    // User Validation
    public UserResponse findUserById(Long userId){
        if (userId == null) {
            throw new IllegalArgumentException("User id cannot be null");
        }

        ApiResponse<UserResponse> response = externalService.getUserById(userId);
        if(response == null || !response.isSuccess() || response.getData() == null){
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }

        return response.getData();
    }

    public void validateUserExists(Long userId) {
        findUserById(userId);
    }


    // City Validation
    public CityResponse findCityById(Long cityId) {
        if (cityId == null) {
            throw new IllegalArgumentException("City id cannot be null");
        }

        ApiResponse<CityResponse> response = externalService.getCityById(cityId);
        if (response == null || !response.isSuccess() || response.getData() == null) {
            throw new ResourceNotFoundException("City not found with id: " + cityId);
        }

        return response.getData();
    }

    public void validateCityExists(Long cityId) {
        findCityById(cityId);
    }

    // Airport Validation
    public AirportResponse findAirportById(Long airportId){
        if(airportId == null){
            throw new IllegalArgumentException("Airport id cannot be null");
        }

        ApiResponse<AirportResponse> response = externalService.getAirportById(airportId);
        if(response == null || !response.isSuccess() || response.getData() == null){
            throw new ResourceNotFoundException("Airport not found with id: " + airportId);
        }

        return response.getData();
    }

    public void validateAirportExists(Long airportId) {
        findAirportById(airportId);
    }

}
