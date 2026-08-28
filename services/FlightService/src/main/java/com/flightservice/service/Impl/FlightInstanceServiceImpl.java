package com.flightservice.service.Impl;

import com.airlineportal.exception.ResourceNotFoundException;
import com.airlineportal.payload.request.Flight.FlightInstanceRequest;
import com.airlineportal.payload.response.Airlines.Aircraft.AircraftResponse;
import com.airlineportal.payload.response.Airlines.Airline.AirlineResponse;
import com.airlineportal.payload.response.Flight.FlightInstanceResponse;
import com.airlineportal.payload.response.Location.Airport.AirportResponse;
import com.airlineportal.utils.Flight.FlightStatus;
import com.flightservice.helper.FlightInstanceHelper;
import com.flightservice.mapper.FlightInstanceMapper;
import com.flightservice.model.Flight;
import com.flightservice.model.FlightInstance;
import com.flightservice.model.FlightSchedule;
import com.flightservice.repository.FlightInstanceRepository;
import com.flightservice.repository.FlightRepository;
import com.flightservice.repository.FlightScheduleRepository;
import com.flightservice.service.FlightInstanceService;
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
public class FlightInstanceServiceImpl implements FlightInstanceService {
    private final FlightInstanceRepository flightInstanceRepository;
    private final FlightRepository flightRepository;
    private final FlightScheduleRepository flightScheduleRepository;

    private final FlightInstanceHelper flightInstanceHelper;

    @Override
    @Transactional
    public FlightInstanceResponse createInstance(Long flightId, Long scheduleId, FlightInstanceRequest request, Long airlineId){
        validateIds(flightId, scheduleId, airlineId);

        // Find flight
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new ResourceNotFoundException("Flight not found with id: " + flightId));

        validateFlightOwnership(flight, airlineId);

        FlightSchedule schedule =
                flightScheduleRepository.findById(scheduleId)
                        .orElseThrow(() -> new ResourceNotFoundException("Flight schedule not found with id: " + scheduleId));

        // Verify schedule belongs to flight
        if(schedule.getFlight() == null || schedule.getFlight().getId() == null || !schedule.getFlight().getId().equals(flightId)) {
            throw new ResourceNotFoundException("Schedule does not belong to this flight");
        }

        flightInstanceHelper.validateCreate(request, flight, schedule);
        FlightInstance instance = FlightInstanceMapper.toEntity(request, flight, schedule);

        FlightInstance saved = flightInstanceRepository.save(instance);
        return convertToInstanceResponse(saved);
    }

    @Override
    public FlightInstanceResponse getById(Long instanceId) {
        FlightInstance flightInstance = flightInstanceHelper.findById(instanceId);

        return convertToInstanceResponse(flightInstance);
    }


    @Override
    public Page<FlightInstanceResponse> getByFlight(Long flightId, Pageable pageable){
        if(flightId == null){
            throw new IllegalArgumentException("Flight id cannot be null");
        }

        return flightInstanceRepository.findByFlightId(flightId, pageable)
                .map(this::convertToInstanceResponse);
    }

    @Override
    public Page<FlightInstanceResponse> getByAirline(Long airlineId, Pageable pageable) {
        if(airlineId == null){
            throw new IllegalArgumentException("Airline id cannot be null");
        }

        return flightInstanceRepository.findByFlightAirlineId(airlineId, pageable)
                .map(this::convertToInstanceResponse);
    }

    @Override
    public Page<FlightInstanceResponse> search(Long airlineId, Long departureAirportId, Long arrivalAirportId, Long flightId,
                                        LocalDateTime dayStart, LocalDateTime dayEnd, Pageable pageable){
        if(airlineId == null){
            throw new IllegalArgumentException("Airline id cannot be null");
        }
        if(dayStart != null && dayEnd != null && !dayEnd.isAfter(dayStart)){
            throw new IllegalArgumentException("Day end must be after day start");
        }

        return flightInstanceRepository.findByAirlineId(airlineId, departureAirportId, arrivalAirportId, flightId,
                dayStart, dayEnd, pageable)
                .map(this::convertToInstanceResponse);
    }


    @Override
    @Transactional
    public FlightInstanceResponse updateInstance(Long instanceId, FlightInstanceRequest request, Long airlineId){
        if(airlineId == null){
            throw new IllegalArgumentException("Airline id cannot be null");
        }

        FlightInstance flightInstance = flightInstanceHelper.findByIdAndAirline(instanceId, airlineId);

        FlightInstanceMapper.updateEntity(flightInstance, request);

        FlightInstance updatedFlightInstance = flightInstanceRepository.save(flightInstance);
        return convertToInstanceResponse(updatedFlightInstance);
    }


    @Override
    @Transactional
    public void deleteInstance(Long instanceId, Long airlineId) {
        if(airlineId == null){
            throw new IllegalArgumentException("Airline id cannot be null");
        }

        FlightInstance flightInstance = flightInstanceHelper.findByIdAndAirline(instanceId, airlineId);
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




    private void validateIds(Long flightId, Long scheduleId, Long airlineId){
        if(flightId == null){
            throw new IllegalArgumentException("Flight id cannot be null");
        }
        if(scheduleId == null){
            throw new IllegalArgumentException("Schedule id cannot be null");
        }
        if(airlineId == null){
            throw new IllegalArgumentException("Airline id cannot be null");
        }
    }

    private void validateFlightOwnership(Flight flight, Long airlineId){
        if(flight.getAirlineId() == null || !flight.getAirlineId().equals(airlineId)){
            throw new ResourceNotFoundException("Flight not found or you are not authorized");
        }
    }


}
