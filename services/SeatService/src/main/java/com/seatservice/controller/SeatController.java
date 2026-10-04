package com.seatservice.controller;

import com.airlineportal.payload.request.Seat.SeatCreateRequest;
import com.airlineportal.payload.response.ApiResponse;
import com.airlineportal.payload.response.Seat.SeatResponse;
import com.seatservice.service.SeatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/seats")
public class SeatController {
    private final SeatService seatService;

    @PostMapping
    public ApiResponse<SeatResponse> createSeat(@Valid @RequestBody SeatCreateRequest request){
        return ApiResponse.success(seatService.createSeat(request));
    }

    @GetMapping("/{seatId}")
    public ApiResponse<SeatResponse> getById(@PathVariable Long seatId){
        return ApiResponse.success(seatService.getById(seatId));
    }

    @GetMapping("/flight-instance/{flightInstanceId}")
    public ApiResponse<List<SeatResponse>> getByFlightInstance(@PathVariable Long flightInstanceId){
        return ApiResponse.success(seatService.getByFlightInstance(flightInstanceId));
    }

    @GetMapping("/flight-instance/{flightInstanceId}/available")
    public ApiResponse<List<SeatResponse>> getAvailableSeats(@PathVariable Long flightInstanceId){
        return ApiResponse.success(seatService.getAvailableSeats(flightInstanceId));
    }

    @GetMapping("/flight-instance/{flightInstanceId}/{seatNumber}")
    public ApiResponse<SeatResponse> getBySeatNumber(@PathVariable Long flightInstanceId, @PathVariable String seatNumber){
        return ApiResponse.success(seatService.getBySeatNumber(flightInstanceId, seatNumber));
    }

    @GetMapping("/booking/{bookingId}")
    public ApiResponse<List<SeatResponse>> getByBookingId(@PathVariable Long bookingId){
        return ApiResponse.success(seatService.getByBookingId(bookingId));
    }


}
