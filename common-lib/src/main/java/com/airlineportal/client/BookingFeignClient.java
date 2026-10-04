package com.airlineportal.client;

import com.airlineportal.payload.response.ApiResponse;
import com.airlineportal.payload.response.Booking.BookingResponse;
import com.airlineportal.payload.response.Payment.PaymentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;


@FeignClient(
        name = "BBOKINGSERVICE",
        path = "/internal/bookings"
)
public interface BookingFeignClient {

    @GetMapping("/{bookingId}")
    ApiResponse<BookingResponse> getBookingById(@PathVariable Long bookingId);

    @GetMapping("/pnr/{pnr}")
    ApiResponse<BookingResponse> getBookingByPnr(@PathVariable String pnr);

    @GetMapping("/user/{userId}")
    ApiResponse<Page<BookingResponse>> getBookingsByUser(@PathVariable Long userId, Pageable pageable);

    @GetMapping("/flight/{flightId}")
    ApiResponse<Page<BookingResponse>> getBookingsByFlight(@PathVariable Long flightId, Pageable pageable);



}
