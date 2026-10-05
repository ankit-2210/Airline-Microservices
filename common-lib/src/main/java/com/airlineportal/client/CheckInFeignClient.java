package com.airlineportal.client;

import com.airlineportal.payload.response.ApiResponse;
import com.airlineportal.payload.response.CheckIn.CheckInResponse;
import org.springframework.web.bind.annotation.*;

public interface CheckInFeignClient {

    @GetMapping("/internal/check-ins/{checkInId}")
    ApiResponse<CheckInResponse> getCheckInById(@PathVariable Long checkInId);

    @GetMapping("/internal/check-ins/booking/{bookingId}/passenger/{passengerId}")
    ApiResponse<CheckInResponse> getByBookingAndPassenger(@PathVariable Long bookingId, @PathVariable Long passengerId);




}
