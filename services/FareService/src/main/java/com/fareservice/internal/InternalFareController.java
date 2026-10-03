package com.fareservice.internal;

import com.airlineportal.payload.request.Fare.FareQuoteRequest;
import com.airlineportal.payload.response.ApiResponse;
import com.airlineportal.payload.response.Fare.FareResponse;
import com.fareservice.service.FareService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/fares")
public class InternalFareController {
    private final FareService fareService;

    @GetMapping("/{fareId}")
    public ApiResponse<FareResponse> getById(@PathVariable Long fareId){
        return ApiResponse.success(fareService.getById(fareId));
    }

    @GetMapping("/flight-instance/{flightInstanceId}")
    public ApiResponse<List<FareResponse>> getByFlightInstance(@PathVariable Long flightInstanceId){
        return ApiResponse.success(fareService.getByFlightInstance(flightInstanceId));
    }

    @PostMapping("/quote")
    public ApiResponse<FareResponse> getFareQuote(@Valid @RequestBody FareQuoteRequest request){
        return ApiResponse.success(fareService.getFareQuote(request));
    }

}
