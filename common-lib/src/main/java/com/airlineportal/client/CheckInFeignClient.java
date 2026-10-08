package com.airlineportal.client;

import com.airlineportal.payload.response.ApiResponse;
import com.airlineportal.payload.response.CheckIn.CheckInResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "CHECKINSERVICE",
        path = "/internal/check-ins"
)
public interface CheckInFeignClient {

    @GetMapping("/{checkInId}")
    ApiResponse<CheckInResponse> getCheckInById(@PathVariable Long checkInId);

    @GetMapping("/booking/{bookingId}/passenger/{passengerId}")
    ApiResponse<CheckInResponse> getByBookingAndPassenger(@PathVariable Long bookingId, @PathVariable Long passengerId);




}
