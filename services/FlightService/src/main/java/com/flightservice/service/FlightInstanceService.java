package com.flightservice.service;

import com.airlineportal.payload.request.Flight.FlightInstanceRequest;
import com.airlineportal.payload.response.Flight.FlightInstanceResponse;
import com.airlineportal.utils.Flight.FlightStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface FlightInstanceService {

    FlightInstanceResponse createInstance(Long flightId, Long scheduleId, FlightInstanceRequest request, Long airlineId);
    FlightInstanceResponse getById(Long instanceId);

    Page<FlightInstanceResponse> getByFlight(Long flightId, Pageable pageable);
    Page<FlightInstanceResponse> getByAirline(Long airlineId, Pageable pageable);
    Page<FlightInstanceResponse> search(Long airlineId, Long departureAirportId, Long arrivalAirportId, Long flightId, LocalDateTime dayStart, LocalDateTime dayEnd, Pageable pageable);

    FlightInstanceResponse updateInstance(Long instanceId, FlightInstanceRequest request, Long airlineId);

    void deleteInstance(Long instanceId, Long airlineId);


}
