package com.adminservice.service;

import com.airlineportal.payload.response.ApiResponse;
import com.airlineportal.payload.response.Booking.BookingResponse;
import org.springframework.web.bind.annotation.*;


public interface AdminBookingService {
    ApiResponse<BookingResponse> getBookingById(@PathVariable Long bookingId);

    ApiResponse<BookingResponse> getBookingByPnr(@PathVariable String pnr);



}
