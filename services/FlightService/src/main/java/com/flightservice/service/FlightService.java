package com.flightservice.service;

import com.flightservice.model.Flight;
import com.microservices.payload.request.Flight.FlightRequest;
import com.microservices.payload.response.Flight.FlightResponse;
import com.microservices.utils.Flight.FlightStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FlightService {
    FlightResponse createFlight(Long airlineId, FlightRequest flightRequest);
    Page<FlightResponse> getFlightByAirline(Long airlineId, Long departureAirportId, Long arrivalAirportId, Pageable pageable);
    FlightResponse getFlightById(Long id);
    FlightResponse updateFlight(Long airlineId, Long id, FlightRequest flightRequest);
    FlightResponse changeStatus(Long airlineId, Long id, FlightStatus flightStatus);
    void deleteFlight(Long airlineId, Long id);




}
