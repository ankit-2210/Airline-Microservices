package com.bookingservice.internal;


import com.airlineportal.payload.response.ApiResponse;
import com.airlineportal.payload.response.Booking.BookingResponse;
import com.bookingservice.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/bookings")
public class InternalBookingController {
    private final BookingService bookingService;

    @GetMapping("/{bookingId}")
    public ApiResponse<BookingResponse> getBookingById(@PathVariable Long bookingId){
        return ApiResponse.success(bookingService.getById(bookingId));
    }

    @GetMapping("/pnr/{pnr}")
    public ApiResponse<BookingResponse> getBookingByPnr(@PathVariable String pnr) {
        return ApiResponse.success(bookingService.getByPnr(pnr));
    }

    @GetMapping("/user/{userId}")
    public ApiResponse<Page<BookingResponse>> getBookingsByUser(@PathVariable Long userId, Pageable pageable){
        return ApiResponse.success(bookingService.getByUser(userId, pageable));
    }

    @GetMapping("/flight/{flightId}")
    public ApiResponse<Page<BookingResponse>> getBookingsByFlight(@PathVariable Long flightId, Pageable pageable){
        return ApiResponse.success(bookingService.getByFlight(flightId, pageable));
    }




}
