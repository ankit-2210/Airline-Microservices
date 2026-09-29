package com.bookingservice.service;

import com.airlineportal.payload.request.Booking.BookingRequest;
import com.airlineportal.payload.response.Booking.BookingResponse;
import org.springframework.data.domain.*;

public interface BookingService {

    BookingResponse createBooking(BookingRequest request);
    BookingResponse getById(Long bookingId);
    BookingResponse getByPnr(String pnr);

    Page<BookingResponse> getByUser(Long userId, Pageable pageable);
    Page<BookingResponse> getByFlight(Long flightId, Pageable pageable);
    Page<BookingResponse> getAll(Pageable pageable);

    BookingResponse cancelBooking(Long bookingId, Long userId, String reason);


}
