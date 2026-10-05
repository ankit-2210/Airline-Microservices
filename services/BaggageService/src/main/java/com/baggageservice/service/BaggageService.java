package com.baggageservice.service;

import com.airlineportal.payload.request.Baggage.BaggageCreateRequest;
import com.airlineportal.payload.request.Baggage.BaggageStatusUpdateRequest;
import com.airlineportal.payload.response.Baggage.BaggageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.*;

public interface BaggageService {

    BaggageResponse addBaggage(BaggageCreateRequest request);
    BaggageResponse getById(Long baggageId);

    List<BaggageResponse> getByBookingId(Long bookingId);
    List<BaggageResponse> getByPassengerId(Long passengerId);
    List<BaggageResponse> getByFlightInstanceId(Long flightInstanceId);

    Page<BaggageResponse> getByBookingId(Long bookingId, Pageable pageable);

    Page<BaggageResponse> getByPassengerId(Long passengerId, Pageable pageable);
    Page<BaggageResponse> getByFlightInstanceId(Long flightInstanceId, Pageable pageable);

    BaggageResponse updateStatus(Long baggageId, BaggageStatusUpdateRequest request);
    BaggageResponse cancelBaggage(Long baggageId);


}
