package com.flightservice.service.Impl;

import com.flightservice.mapper.FlightInstanceMapper;
import com.flightservice.model.Flight;
import com.flightservice.model.FlightInstance;
import com.flightservice.model.FlightSchedule;
import com.flightservice.repository.FlightInstanceRepository;
import com.flightservice.repository.FlightRepository;
import com.flightservice.repository.FlightScheduleRepository;
import com.flightservice.service.FlightInstanceService;
import com.microservices.exception.ResourceNotFoundException;
import com.microservices.payload.request.Flight.FlightInstanceRequest;
import com.microservices.payload.response.Airline.AircraftResponse;
import com.microservices.payload.response.Airline.AirlineResponse;
import com.microservices.payload.response.Airport.AirportResponse;
import com.microservices.payload.response.Flight.FlightInstanceResponse;
import com.microservices.utils.Flight.FlightStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FlightInstanceServiceImpl implements FlightInstanceService {
    private final FlightInstanceRepository flightInstanceRepository;
    private final FlightRepository flightRepository;
    private final FlightScheduleRepository flightScheduleRepository;

    private Flight findFlightById(Long flightId) {
        return flightRepository.findById(flightId)
                .orElseThrow(() -> new ResourceNotFoundException("Flight not found with id: " + flightId));
    }

    private Flight findFlight(Long id){
        return flightRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Flight not found with id: " + id));
    }

    private FlightSchedule findSchedule(Long id){
        return flightScheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found with id: " + id));
    }

    private FlightInstance findInstance(Long id) {
        return flightInstanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Flight instance not found with id: " + id));
    }

    private FlightInstance findByAirline(Long airlineId, Long id){
        return flightInstanceRepository.findByIdAndFlightAirlineId(id, airlineId)
                .orElseThrow(() -> new ResourceNotFoundException("Flight instance not found"));
    }


    @Transactional
    @Override
    public FlightInstanceResponse createFlightInstance(Long airlineId, FlightInstanceRequest flightInstanceRequest) {
        Flight flight = findFlightById(flightInstanceRequest.getFlightId());
        if(!flight.getAirlineId().equals(airlineId)){
            throw new ResourceNotFoundException("Flight does not belong to airline");
        }
        FlightSchedule schedule = findSchedule(flightInstanceRequest.getScheduleId());
        FlightInstance flightInstance = FlightInstanceMapper.toEntity(flightInstanceRequest, flight, schedule);
        FlightInstance savedFlightInstance = flightInstanceRepository.save(flightInstance);
        return convertToInstanceResponse(savedFlightInstance);
    }

    @Override
    public FlightInstanceResponse getFlightInstanceById(Long id) {
        FlightInstance flightInstance = findInstance(id);
        return convertToInstanceResponse(flightInstance);
    }

    @Override
    public Page<FlightInstanceResponse> getByAirlineId(Long airlineId, Long departureAirportId, Long arrivalAirportId, Long flightId, LocalDate onDate, Pageable pageable) {
        return flightInstanceRepository.findByAirlineId(airlineId, departureAirportId, arrivalAirportId, flightId,
                        onDate != null ? onDate.atStartOfDay() : null,
                        onDate != null ? onDate.plusDays(1).atStartOfDay() : null,
                        pageable
                )
                .map(this::convertToInstanceResponse);
    }

    @Override
    public Page<FlightInstanceResponse> getByFlightId(Long flightId, Pageable pageable) {
        return flightInstanceRepository.findByFlightId(flightId, pageable)
                .map(this::convertToInstanceResponse);
    }

    @Transactional
    @Override
    public FlightInstanceResponse updateFlightInstance(Long airlineId, Long id, FlightInstanceRequest flightInstanceRequest) {
        FlightInstance flightInstance = findByAirline(airlineId, id);
        FlightInstanceMapper.updateEntity(flightInstance, flightInstanceRequest);

        FlightInstance updatedFlightInstance = flightInstanceRepository.save(flightInstance);
        return convertToInstanceResponse(updatedFlightInstance);
    }

    @Transactional
    @Override
    public FlightInstanceResponse changeStatus(Long airlineId, Long id, FlightStatus flightStatus) {
        FlightInstance flightInstance = findByAirline(airlineId, id);
        flightInstance.setFlightStatus(flightStatus);

        FlightInstance updatedFlightInstance = flightInstanceRepository.save(flightInstance);
        return convertToInstanceResponse(updatedFlightInstance);
    }

    @Transactional
    @Override
    public FlightInstanceResponse updateAvailableSeats(Long airlineId, Long id, Integer availableSeats) {
        FlightInstance flightInstance = findByAirline(airlineId, id);
        if(availableSeats < 0)
            availableSeats = 0;
        if(availableSeats > flightInstance.getTotalSeats()){
            availableSeats = flightInstance.getTotalSeats();
        }
        flightInstance.setAvailableSeats(availableSeats);
        FlightInstance updatedFlightInstance = flightInstanceRepository.save(flightInstance);
        return convertToInstanceResponse(updatedFlightInstance);
    }

    @Transactional
    @Override
    public FlightInstanceResponse toggleActive(Long airlineId, Long id, Boolean active) {
        FlightInstance flightInstance = findByAirline(airlineId, id);
        flightInstance.setActive(active);

        FlightInstance updatedFlightInstance = flightInstanceRepository.save(flightInstance);
        return convertToInstanceResponse(updatedFlightInstance);
    }

    @Transactional
    @Override
    public void deleteFlightInstance(Long airlineId, Long id) {
        FlightInstance flightInstance = findByAirline(airlineId, id);
        flightInstanceRepository.delete(flightInstance);
    }

    private FlightInstanceResponse convertToInstanceResponse(FlightInstance flightInstance){
        Flight flight = flightInstance.getFlight();

        AirlineResponse airlineResponse = AirlineResponse.builder()
                .id(flight.getAirlineId())
                .build();
        AircraftResponse aircraftResponse = AircraftResponse.builder()
                .id(flight.getAircraftId())
                .build();
        AirportResponse departureAirport = AirportResponse.builder()
                .id(flight.getDepartureAirportId())
                .build();
        AirportResponse arrivalAirport = AirportResponse.builder()
                .id(flight.getArrivalAirportId())
                .build();
        return FlightInstanceMapper.toResponse(flightInstance, aircraftResponse, airlineResponse, departureAirport, arrivalAirport);
    }
}
