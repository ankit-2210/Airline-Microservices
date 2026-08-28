package com.flightservice.service.Impl;

import com.airlineportal.payload.request.Flight.FlightRequest;
import com.airlineportal.payload.response.Flight.FlightResponse;
import com.airlineportal.utils.Flight.FlightStatus;
import com.flightservice.helper.FlightHelper;
import com.flightservice.mapper.FlightMapper;
import com.flightservice.model.Flight;
import com.flightservice.repository.FlightRepository;
import com.flightservice.service.FlightService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FlightServiceImpl implements FlightService {
    private final FlightRepository flightRepository;
    private final FlightHelper flightHelper;

    @Override
    @Transactional
    public FlightResponse createFlight(Long airlineId, FlightRequest flightRequest) {
        if(airlineId == null){
            throw new IllegalArgumentException("Airline id cannot be null");
        }

        if(flightRequest == null){
            throw new IllegalArgumentException("Flight request cannot be null");
        }

        flightRequest.setAirlineId(airlineId);
        flightHelper.validateCreate(flightRequest);

        Flight flight = FlightMapper.toEntity(flightRequest);

        flight.setAirlineId(airlineId);
        flightHelper.normalizeEntity(flight);

        Flight savedFlight = flightRepository.save(flight);
        return FlightMapper.toResponse(savedFlight);
    }

    @Override
    public FlightResponse getById(Long flightId) {
        Flight flight = flightHelper.findById(flightId);

        return FlightMapper.toResponse(flight);
    }

    @Override
    public Page<FlightResponse> getAllByAirline(Long airlineId, Pageable pageable) {
        if(airlineId == null){
            throw new IllegalArgumentException("Airline id cannot be null");
        }

        return flightRepository.findByAirlineId(airlineId, pageable)
                .map(FlightMapper::toResponse);
    }

    @Override
    public Page<FlightResponse> searchByRoute(Long airlineId, Long departureAirportId, Long arrivalAirportId, Pageable pageable) {
        if(airlineId == null){
            throw new IllegalArgumentException("Airline id cannot be null");
        }
        if(departureAirportId == null){
            throw new IllegalArgumentException("Departure airport id cannot be null");
        }
        if(arrivalAirportId == null){
            throw new IllegalArgumentException("Arrival airport id cannot be null");
        }

        return flightRepository.findByAirlineIdAndDepartureAirportIdAndArrivalAirportId(
                airlineId,
                departureAirportId,
                arrivalAirportId,
                pageable
        )
                .map(FlightMapper::toResponse);

    }

    @Override
    public Page<FlightResponse> getByStatus(FlightStatus flightStatus, Pageable pageable) {
        if(flightStatus == null){
            throw new IllegalArgumentException("Flight status cannot be null");
        }

        return flightRepository.findByFlightStatus(flightStatus, pageable)
                .map(FlightMapper::toResponse);
    }

    @Override
    public Page<FlightResponse> getByDepartureAirport(Long departureAirportId, Pageable pageable) {
        if(departureAirportId == null){
            throw new IllegalArgumentException("Departure airport id cannot be null");
        }

        return flightRepository.findByDepartureAirportId(departureAirportId, pageable)
                .map(FlightMapper::toResponse);
    }

    @Override
    public Page<FlightResponse> getByArrivalAirport(Long arrivalAirportId, Pageable pageable) {
        if(arrivalAirportId == null){
            throw new IllegalArgumentException("Arrival airport id cannot be null");
        }

        return flightRepository.findByArrivalAirportId(arrivalAirportId, pageable)
                .map(FlightMapper::toResponse);
    }

    @Override
    public Page<FlightResponse> getByRoute(Long departureAirportId, Long arrivalAirportId, Pageable pageable) {
        if(departureAirportId == null){
            throw new IllegalArgumentException("Departure airport id cannot be null");
        }
        if(arrivalAirportId == null){
            throw new IllegalArgumentException("Arrival airport id cannot be null");
        }
        if(departureAirportId.equals(arrivalAirportId)){
            throw new IllegalArgumentException("Departure and arrival airports cannot be the same");
        }

        return flightRepository.findByDepartureAirportIdAndArrivalAirportId(departureAirportId, arrivalAirportId, pageable)
                .map(FlightMapper::toResponse);
    }

    @Override
    public Page<FlightResponse> getUpcomingFlights(Long airlineId, Pageable pageable) {
        if(airlineId == null){
            throw new IllegalArgumentException("Airline id cannot be null");
        }

        return flightRepository.findUpcomingFlight(airlineId, pageable)
                .map(FlightMapper::toResponse);
    }

    @Override
    public Page<FlightResponse> searchFlightsByDate(Long departureAirportId, Long arrivalAirportId, LocalDate departureDate, Pageable pageable) {
        if(departureAirportId == null){
            throw new IllegalArgumentException("Departure airport id cannot be null");
        }
        if(arrivalAirportId == null){
            throw new IllegalArgumentException("Arrival airport id cannot be null");
        }
        if(departureDate == null){
            throw new IllegalArgumentException("Departure date cannot be null");
        }
        if(departureAirportId.equals(arrivalAirportId)){
            throw new IllegalArgumentException("Departure and arrival airports cannot be the same");
        }

        return flightRepository.searchFlightsByDate(departureAirportId, arrivalAirportId, departureDate, pageable)
                .map(FlightMapper::toResponse);
    }

    @Override
    @Transactional
    public FlightResponse updateFlight(Long flightId, FlightRequest request, Long airlineId) {
        if(airlineId == null){
            throw new IllegalArgumentException("Airline id cannot be null");
        }

        Flight flight = flightHelper.findByAirlineAndId(airlineId, flightId);
        request.setAirlineId(airlineId);

        flightHelper.validateUpdate(flight, request);
        FlightMapper.updateEntity(flight, request);

        flight.setAirlineId(airlineId);
        flightHelper.normalizeEntity(flight);

        Flight updatedFlight = flightRepository.save(flight);
        return FlightMapper.toResponse(updatedFlight);
    }

    @Override
    @Transactional
    public void deleteFlight(Long flightId, Long airlineId) {
        Flight flight = flightHelper.findByAirlineAndId(airlineId, flightId);

        flightHelper.validateCanDelete(flight);
        flightRepository.delete(flight);
    }

    @Override
    @Transactional
    public FlightResponse changeStatus(Long flightId, FlightStatus flightStatus, Long airlineId) {
        if(airlineId == null){
            throw new IllegalArgumentException("Airline id cannot be null");
        }
        if(flightStatus == null){
            throw new IllegalArgumentException("Flight status cannot be null");
        }

        Flight flight = flightHelper.findByAirlineAndId(airlineId, flightId);
        flight.setFlightStatus(flightStatus);

        Flight updatedFlight = flightRepository.save(flight);
        return FlightMapper.toResponse(updatedFlight);
    }
}
