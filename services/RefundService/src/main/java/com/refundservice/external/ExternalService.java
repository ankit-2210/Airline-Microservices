package com.refundservice.external;


import com.airlineportal.payload.response.ApiResponse;
import com.airlineportal.payload.response.Booking.BookingResponse;
import com.airlineportal.payload.response.Flight.FlightInstanceResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface ExternalService {

    @GetMapping("/internal/bookings/{bookingId}")
    ApiResponse<BookingResponse> getBookingById(@PathVariable Long bookingId);





}
