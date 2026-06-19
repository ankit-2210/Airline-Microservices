package com.flightservice.service;

import com.microservices.payload.request.Flight.FlightInstanceRequest;
import com.microservices.payload.response.Flight.FlightInstanceResponse;
import com.microservices.utils.Flight.FlightStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface FlightInstanceService {
    FlightInstanceResponse createFlightInstance(Long airlineId, FlightInstanceRequest flightInstanceRequest);

    FlightInstanceResponse getFlightInstanceById(Long id);

    Page<FlightInstanceResponse> getByAirlineId(Long airlineId, Long departureAirportId, Long arrivalAirportId, Long flightId, LocalDate onDate, Pageable pageable);
    Page<FlightInstanceResponse> getByFlightId(Long flightId, Pageable pageable);

    FlightInstanceResponse updateFlightInstance(Long airlineId, Long id, FlightInstanceRequest flightInstanceRequest);
    FlightInstanceResponse changeStatus(Long airlineId, Long id, FlightStatus flightStatus);

    FlightInstanceResponse updateAvailableSeats(Long airlineId, Long id, Integer availableSeats);
    FlightInstanceResponse toggleActive(Long airlineId, Long id, Boolean active);

    void deleteFlightInstance(Long airlineId, Long id);


}
