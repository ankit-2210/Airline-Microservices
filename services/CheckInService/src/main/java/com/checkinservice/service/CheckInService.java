package com.checkinservice.service;

import com.airlineportal.payload.request.CheckIn.CheckInRequest;
import com.airlineportal.payload.response.CheckIn.CheckInResponse;
import org.springframework.data.domain.*;

public interface CheckInService {

    CheckInResponse checkIn(CheckInRequest request);
    CheckInResponse getById(Long checkInId);
    CheckInResponse getByBookingAndPassenger(Long bookingId, Long passengerId);

    Page<CheckInResponse> getByBookingId(Long bookingId, Pageable pageable);
    Page<CheckInResponse> getByPassengerId(Long passengerId, Pageable pageable);
    Page<CheckInResponse> getByFlightInstanceId(Long flightInstanceId, Pageable pageable);

    CheckInResponse cancelCheckIn(Long checkInId);


}
