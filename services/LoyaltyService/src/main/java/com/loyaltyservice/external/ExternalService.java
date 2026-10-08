package com.loyaltyservice.external;

import com.airlineportal.payload.response.ApiResponse;
import com.airlineportal.payload.response.Booking.BookingResponse;
import org.springframework.web.bind.annotation.*;

public interface ExternalService {


    @GetMapping("/internal/bookings/{bookingId}")
    ApiResponse<BookingResponse> getBookingById(@PathVariable Long bookingId);



}
