package com.seatservice.service;

import com.airlineportal.payload.request.Seat.SeatCreateRequest;
import com.airlineportal.payload.response.Seat.SeatResponse;

import java.util.*;

public interface SeatService {

    SeatResponse createSeat(SeatCreateRequest request);
    SeatResponse getById(Long seatId);
    SeatResponse getBySeatNumber(Long flightInstanceId, String seatNumber);

    List<SeatResponse> getByFlightInstance(Long flightInstanceId);
    List<SeatResponse> getAvailableSeats(Long flightInstanceId);

    SeatResponse bookSeat(Long flightInstanceId, String seatNumber, Long bookingId);
    SeatResponse releaseSeat(Long flightInstanceId, String seatNumber);

    List<SeatResponse> getByBookingId(Long bookingId);



}
