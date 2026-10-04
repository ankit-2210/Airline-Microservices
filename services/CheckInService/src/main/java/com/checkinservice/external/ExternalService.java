package com.checkinservice.external;


import com.airlineportal.payload.response.ApiResponse;
import com.airlineportal.payload.response.Booking.BookingResponse;
import com.airlineportal.payload.response.Flight.FlightInstanceResponse;
import com.airlineportal.payload.response.Seat.SeatResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface ExternalService {

    @GetMapping("/internal/bookings/{bookingId}")
    ApiResponse<BookingResponse> getBookingById(@PathVariable Long bookingId);

    @GetMapping("/internal/bookings/user/{userId}")
    ApiResponse<Page<BookingResponse>> getBookingsByUser(@PathVariable Long userId, Pageable pageable);


    @GetMapping("/internal/flight-instances/{instanceId}")
    ApiResponse<FlightInstanceResponse> getInstanceById(@PathVariable Long instanceId);


    @GetMapping("/internal/seats/flight-instance/{flightInstanceId}/{seatNumber}")
    ApiResponse<SeatResponse> getSeat(@PathVariable Long flightInstanceId, @PathVariable String seatNumber);


}
