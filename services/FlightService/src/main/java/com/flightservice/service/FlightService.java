package com.flightservice.service;

import com.airlineportal.payload.request.Flight.FlightRequest;
import com.airlineportal.payload.response.Flight.FlightResponse;
import com.airlineportal.utils.Flight.FlightStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface FlightService {
    FlightResponse createFlight(Long airlineId, FlightRequest flightRequest);

    FlightResponse getById(Long flightId);

    Page<FlightResponse> getAllByAirline(Long airlineId, Pageable pageable);
    Page<FlightResponse> searchByRoute(Long airlineId, Long departureAirportId, Long arrivalAirportId, Pageable pageable);
    Page<FlightResponse> getByStatus(FlightStatus flightStatus, Pageable pageable);

    Page<FlightResponse> getByDepartureAirport(Long departureAirportId, Pageable pageable);
    Page<FlightResponse> getByArrivalAirport(Long arrivalAirportId, Pageable pageable);
    Page<FlightResponse> getByRoute(Long departureAirportId, Long arrivalAirportId, Pageable pageable);
    Page<FlightResponse> getUpcomingFlights(Long airlineId, Pageable pageable);
    Page<FlightResponse> searchFlightsByDate(Long departureAirportId, Long arrivalAirportId, LocalDate departureDate, Pageable pageable);

    FlightResponse updateFlight(Long flightId, FlightRequest request, Long airlineId);

    void deleteFlight(Long flightId, Long airlineId);

    FlightResponse changeStatus(Long flightId, FlightStatus flightStatus, Long airlineId);


}
