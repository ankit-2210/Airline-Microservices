package com.flightservice.service.Impl;

import com.flightservice.mapper.FlightMapper;
import com.flightservice.model.Flight;
import com.flightservice.repository.FlightRepository;
import com.flightservice.service.FlightService;
import com.microservices.exception.ResourceAlreadyExistsException;
import com.microservices.exception.ResourceNotFoundException;
import com.microservices.payload.request.Flight.FlightRequest;
import com.microservices.payload.response.Flight.FlightResponse;
import com.microservices.utils.Flight.FlightStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FlightServiceImpl implements FlightService {
    private final FlightRepository flightRepository;

    private Flight findFlight(Long id){
        return flightRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Flight not found with id: " + id));
    }

    private Flight findFlightByAirline(Long airlineId, Long id){
        return flightRepository.findByAirlineIdAndId(airlineId, id)
                .orElseThrow(() -> new ResourceNotFoundException("Flight not found with id: " + id));
    }

    private void validate(FlightRequest flightRequest){
        if (flightRequest.getDepartureAirportId().equals(flightRequest.getArrivalAirportId())) {
            throw new IllegalArgumentException("Departure and arrival airport cannot be same");
        }
        if (flightRequest.getScheduledArrival().isBefore(flightRequest.getScheduledDeparture())) {
            throw new IllegalArgumentException("Arrival must be after departure");
        }
    }

    @Transactional
    @Override
    public FlightResponse createFlight(Long airlineId, FlightRequest flightRequest) {
        if(flightRepository.existsByFlightNumber(flightRequest.getFlightNumber().toUpperCase())){
            throw new ResourceAlreadyExistsException("Flight number already exist");
        }
        validate(flightRequest);
        Flight flight = FlightMapper.toEntity(flightRequest);
        flight.setAirlineId(airlineId);

        Flight savedFlight = flightRepository.save(flight);
        return FlightMapper.toResponse(savedFlight);
    }

    @Override
    public Page<FlightResponse> getFlightByAirline(Long airlineId, Long departureAirportId, Long arrivalAirportId, Pageable pageable) {
        if(departureAirportId != null && arrivalAirportId != null){
            return flightRepository.findByAirlineIdAndDepartureAirportIdAndArrivalAirportId(airlineId, departureAirportId, arrivalAirportId, pageable)
                    .map(FlightMapper::toResponse);
        }
        return flightRepository.findByAirlineId(airlineId, pageable)
                .map(FlightMapper::toResponse);
    }

    @Override
    public FlightResponse getFlightById(Long id) {
        return FlightMapper.toResponse(findFlight(id));
    }

    @Transactional
    @Override
    public FlightResponse updateFlight(Long airlineId, Long id, FlightRequest flightRequest) {
        Flight flight = findFlightByAirline(airlineId, id);
        if(flightRepository.existsByFlightNumberAndIdNot(flightRequest.getFlightNumber().toUpperCase(), id)){
            throw new ResourceAlreadyExistsException("Flight number already exists");
        }

        validate(flightRequest);
        FlightMapper.updateEntity(flight, flightRequest);
        Flight updatedFlight = flightRepository.save(flight);
        return FlightMapper.toResponse(updatedFlight);
    }

    @Transactional
    @Override
    public FlightResponse changeStatus(Long airlineId, Long id, FlightStatus flightStatus) {
        Flight flight = findFlightByAirline(airlineId, id);
        flight.setFlightStatus(flightStatus);

        if (flightStatus == FlightStatus.DEPARTED) {
            flight.setActualDeparture(LocalDateTime.now());
        }
        if (flightStatus == FlightStatus.ARRIVED) {
            flight.setActualArrival(LocalDateTime.now());
        }

        Flight updatedFlight = flightRepository.save(flight);
        return FlightMapper.toResponse(updatedFlight);
    }

    @Transactional
    @Override
    public void deleteFlight(Long airlineId, Long id) {
        Flight flight = flightRepository.findByAirlineIdAndId(airlineId, id)
                .orElseThrow(() -> new ResourceNotFoundException("Flight not found with id: " + id));
        flightRepository.delete(flight);
    }
}
