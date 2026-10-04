package com.seatservice.internal;


import com.airlineportal.payload.response.ApiResponse;
import com.airlineportal.payload.response.Seat.SeatResponse;
import com.seatservice.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/seats")
public class InternalSeatController {
    private final SeatService seatService;

    @PostMapping("/book")
    public ApiResponse<SeatResponse> bookSeat(@RequestParam Long flightInstanceId, @RequestParam String seatNumber, @RequestParam Long bookingId){
        return ApiResponse.success(seatService.bookSeat(flightInstanceId, seatNumber, bookingId));
    }

    @PostMapping("/release")
    public ApiResponse<SeatResponse> releaseSeat(@RequestParam Long flightInstanceId, @RequestParam String seatNumber){
        return ApiResponse.success(seatService.releaseSeat(flightInstanceId, seatNumber));
    }


}
