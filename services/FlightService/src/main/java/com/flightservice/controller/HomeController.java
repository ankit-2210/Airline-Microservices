package com.flightservice.controller;

import com.microservices.payload.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public ApiResponse<String> HomeController(){
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setSuccess(true);
        apiResponse.setMessage("Flight Operations Service manages Flights, Flight Schedules, and Flight Instances." +
                "It represents the core operational of the flight lifecycle");
        apiResponse.setData("Welcome!");
        return apiResponse;
    }
}
