package com.checkinservice.internal;

import com.airlineportal.payload.response.ApiResponse;
import com.airlineportal.payload.response.CheckIn.CheckInResponse;
import com.checkinservice.service.CheckInService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/check-ins")
public class InternalCheckInController {
    private final CheckInService checkInService;

    @GetMapping("/{checkInId}")
    public ApiResponse<CheckInResponse> getCheckInById(@PathVariable Long checkInId) {
        return ApiResponse.success(checkInService.getById(checkInId));
    }

    @GetMapping("/booking/{bookingId}/passenger/{passengerId}")
    public ApiResponse<CheckInResponse> getByBookingAndPassenger(@PathVariable Long bookingId, @PathVariable Long passengerId){
        return ApiResponse.success(checkInService.getByBookingAndPassenger(bookingId, passengerId));
    }



}
