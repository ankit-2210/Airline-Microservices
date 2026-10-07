package com.airlineportal.client;


import com.airlineportal.payload.response.ApiResponse;
import com.airlineportal.payload.response.Baggage.BaggageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(
        name = "BAGGAGESERVICE",
        path = "/internal/bookings"
)
public interface BaggageFeignClient {

    @GetMapping("/{baggageId}")
    ApiResponse<BaggageResponse> getById(@PathVariable Long baggageId);

    @GetMapping("/booking/{bookingId}")
    ApiResponse<List<BaggageResponse>> getByBookingId(@PathVariable Long bookingId);

    @GetMapping("/passenger/{passengerId}")
    ApiResponse<List<BaggageResponse>> getByPassengerId(@PathVariable Long passengerId);



}
