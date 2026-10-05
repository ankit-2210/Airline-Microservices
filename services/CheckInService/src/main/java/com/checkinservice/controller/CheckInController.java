package com.checkinservice.controller;


import com.airlineportal.payload.request.CheckIn.CheckInRequest;
import com.airlineportal.payload.response.ApiResponse;
import com.airlineportal.payload.response.CheckIn.CheckInResponse;
import com.checkinservice.helper.CheckInHelper;
import com.checkinservice.service.CheckInService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/check-ins")
public class CheckInController {
    private final CheckInService checkInService;

    @PostMapping
    public ApiResponse<CheckInResponse> checkIn(@RequestBody CheckInRequest request) {
        return ApiResponse.success(checkInService.checkIn(request));
    }

    @GetMapping("/{checkInId}")
    public ApiResponse<CheckInResponse> getById(@PathVariable Long checkInId) {
        return ApiResponse.success(checkInService.getById(checkInId));
    }


    @GetMapping("/booking/{bookingId}/passenger/{passengerId}")
    public ApiResponse<CheckInResponse> getByBookingAndPassenger(@PathVariable Long bookingId, @PathVariable Long passengerId){
        return ApiResponse.success(checkInService.getByBookingAndPassenger(bookingId, passengerId));
    }


    @GetMapping("/booking/{bookingId}")
    public ApiResponse<Page<CheckInResponse>> getByBookingId(@PathVariable Long bookingId, Pageable pageable) {
        return ApiResponse.success(checkInService.getByBookingId(bookingId, pageable));
    }

    @GetMapping("/passenger/{passengerId}")
    public ApiResponse<Page<CheckInResponse>> getByPassengerId(@PathVariable Long passengerId, Pageable pageable) {
        return ApiResponse.success(checkInService.getByPassengerId(passengerId, pageable));
    }

    @GetMapping("/flight-instance/{flightInstanceId}")
    public ApiResponse<Page<CheckInResponse>> getByFlightInstanceId(@PathVariable Long flightInstanceId, Pageable pageable) {
        return ApiResponse.success(checkInService.getByFlightInstanceId(flightInstanceId, pageable));
    }


    @PutMapping("/{checkInId}/cancel")
    public ApiResponse<CheckInResponse> cancelCheckIn(@PathVariable Long checkInId) {
        return ApiResponse.success(checkInService.cancelCheckIn(checkInId));
    }



}
