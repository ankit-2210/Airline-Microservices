package com.baggageservice.controller;

import com.airlineportal.payload.request.Baggage.BaggageCreateRequest;
import com.airlineportal.payload.request.Baggage.BaggageStatusUpdateRequest;
import com.airlineportal.payload.response.ApiResponse;
import com.airlineportal.payload.response.Baggage.BaggageResponse;
import com.baggageservice.service.BaggageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/baggage")
public class BaggageController {
    private final BaggageService baggageService;

    @PostMapping
    public ApiResponse<BaggageResponse> addBaggage(@Valid @RequestBody BaggageCreateRequest request){
        return ApiResponse.success(baggageService.addBaggage(request));
    }

    @GetMapping("/{baggageId}")
    public ApiResponse<BaggageResponse> getById(@PathVariable Long baggageId){
        return ApiResponse.success(baggageService.getById(baggageId));
    }

    @GetMapping("/booking/{bookingId}")
    public ApiResponse<List<BaggageResponse>> getByBookingId(@PathVariable Long bookingId){
        return ApiResponse.success(baggageService.getByBookingId(bookingId));
    }

    @GetMapping("/booking/{bookingId}/page")
    public ApiResponse<Page<BaggageResponse>> getByBookingId(@PathVariable Long bookingId, Pageable pageable){
        return ApiResponse.success(baggageService.getByBookingId(bookingId, pageable));
    }

    @GetMapping("/passenger/{passengerId}")
    public ApiResponse<List<BaggageResponse>> getByPassengerId(@PathVariable Long passengerId){
        return ApiResponse.success(baggageService.getByPassengerId(passengerId));
    }

    @GetMapping("/passenger/{passengerId}/page")
    public ApiResponse<Page<BaggageResponse>> getByPassengerId(@PathVariable Long passengerId, Pageable pageable){
        return ApiResponse.success(baggageService.getByPassengerId(passengerId, pageable));
    }

    @GetMapping("/flight-instance/{flightInstanceId}")
    public ApiResponse<List<BaggageResponse>> getByFlightInstanceId(@PathVariable Long flightInstanceId){
        return ApiResponse.success(baggageService.getByFlightInstanceId(flightInstanceId));
    }

    @GetMapping("/flight-instance/{flightInstanceId}/page")
    public ApiResponse<Page<BaggageResponse>> getByFlightInstanceId(@PathVariable Long flightInstanceId, Pageable pageable){
        return ApiResponse.success(baggageService.getByFlightInstanceId(flightInstanceId, pageable));
    }

    @PutMapping("/{baggageId}/status")
    public ApiResponse<BaggageResponse> updateStatus(@PathVariable Long baggageId, @Valid @RequestBody BaggageStatusUpdateRequest request){
        return ApiResponse.success(baggageService.updateStatus(baggageId, request));
    }

    @PutMapping("/{baggageId}/cancel")
    public ApiResponse<BaggageResponse> cancelBaggage(@PathVariable Long baggageId){
        return ApiResponse.success(baggageService.cancelBaggage(baggageId));
    }



}
