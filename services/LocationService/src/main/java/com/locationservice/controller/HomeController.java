package com.locationservice.controller;

import com.airlineportal.payload.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public ApiResponse<String> HomeController(){
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setSuccess(true);
        apiResponse.setMessage("Hello Controller from Location");
        apiResponse.setData("Welcome!");
        return apiResponse;
    }
}
