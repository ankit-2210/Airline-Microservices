package com.airlineportal.client;

import com.airlineportal.payload.response.ApiResponse;
import com.airlineportal.payload.response.Seat.SeatResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "SEATSERVICE",
        path = "/internal/seats"
)
public interface SeatFeignClient {
    @PostMapping("/book")
    ApiResponse<SeatResponse> bookSeat(@RequestParam Long flightInstanceId, @RequestParam String seatNumber, @RequestParam Long bookingId);

    @PostMapping("/release")
    ApiResponse<SeatResponse> releaseSeat(@RequestParam Long flightInstanceId, @RequestParam String seatNumber);


    @GetMapping("/flight-instance/{flightInstanceId}/{seatNumber}")
    ApiResponse<SeatResponse> getSeat(@PathVariable Long flightInstanceId, @PathVariable String seatNumber);


}
