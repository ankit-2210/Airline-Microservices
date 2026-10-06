package com.baggageservice.internal;


import com.airlineportal.payload.response.ApiResponse;
import com.airlineportal.payload.response.Baggage.BaggageResponse;
import com.baggageservice.service.BaggageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/baggage")
public class InternalBaggageController {
    private final BaggageService baggageService;

    @GetMapping("/{baggageId}")
    public ApiResponse<BaggageResponse> getById(@PathVariable Long baggageId){
        return ApiResponse.success(baggageService.getById(baggageId));
    }

    @GetMapping("/booking/{bookingId}")
    public ApiResponse<List<BaggageResponse>> getByBookingId(@PathVariable Long bookingId){
        return ApiResponse.success(baggageService.getByBookingId(bookingId));
    }

    @GetMapping("/passenger/{passengerId}")
    public ApiResponse<List<BaggageResponse>> getByPassengerId(@PathVariable Long passengerId){
        return ApiResponse.success(baggageService.getByPassengerId(passengerId));
    }



}
