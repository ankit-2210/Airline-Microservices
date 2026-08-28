package com.flightservice.service.Impl;

import com.airlineportal.exception.ResourceNotFoundException;
import com.airlineportal.payload.request.Flight.FlightScheduleRequest;
import com.airlineportal.payload.response.Flight.FlightScheduleResponse;
import com.flightservice.helper.FlightScheduleHelper;
import com.flightservice.mapper.FlightScheduleMapper;
import com.flightservice.model.Flight;
import com.flightservice.model.FlightInstance;
import com.flightservice.model.FlightSchedule;
import com.flightservice.repository.FlightInstanceRepository;
import com.flightservice.repository.FlightRepository;
import com.flightservice.repository.FlightScheduleRepository;
import com.flightservice.service.FlightScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FlightScheduleServiceImpl implements FlightScheduleService {
    private final FlightScheduleRepository flightScheduleRepository;
    private final FlightRepository flightRepository;
    private final FlightScheduleHelper flightScheduleHelper;

    @Override
    @Transactional
    public FlightScheduleResponse createSchedule(Long flightId, FlightScheduleRequest request, Long airlineId) {
        if(flightId == null){
            throw new IllegalArgumentException("Flight id cannot be null");
        }
        if(airlineId == null){
            throw new IllegalArgumentException("Airline id cannot be null");
        }

        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new ResourceNotFoundException("Flight not found with id: " + flightId));

        if(!airlineId.equals(flight.getAirlineId())){
            throw new ResourceNotFoundException("Flight not found or you are not authorized");
        }


        flightScheduleHelper.validateCreate(request, flight);

        FlightSchedule schedule = FlightScheduleMapper.toEntity(request);
        schedule.setFlight(flight);

        FlightSchedule savedSchedule = flightScheduleRepository.save(schedule);
        return FlightScheduleMapper.toResponse(savedSchedule);
    }

    @Override
    public FlightScheduleResponse getById(Long scheduleId) {
        FlightSchedule schedule = flightScheduleHelper.findById(scheduleId);

        return FlightScheduleMapper.toResponse(schedule);
    }

    @Override
    public Page<FlightScheduleResponse> getByFlight(Long flightId, Pageable pageable) {
        if(flightId == null){
            throw new IllegalArgumentException("Flight id cannot be null");
        }

        return flightScheduleRepository.findByFlightId(flightId, pageable)
                .map(FlightScheduleMapper::toResponse);
    }

    @Override
    public Page<FlightScheduleResponse> getByAirline(Long airlineId, Pageable pageable) {
        if(airlineId == null){
            throw new IllegalArgumentException("Airline id cannot be null");
        }

        return flightScheduleRepository.findByFlightAirlineId(airlineId, pageable)
                .map(FlightScheduleMapper::toResponse);
    }

    @Override
    public Page<FlightScheduleResponse> getByRoute(Long departureAirportId, Long arrivalAirportId, Pageable pageable) {
        if(departureAirportId == null){
            throw new IllegalArgumentException("Departure airport id cannot be null");
        }
        if(arrivalAirportId == null){
            throw new IllegalArgumentException("Arrival airport id cannot be null");
        }
        if(departureAirportId.equals(arrivalAirportId)){
            throw new IllegalArgumentException("Departure and arrival airports cannot be the same");
        }

        return flightScheduleRepository.findByFlightDepartureAirportIdAndFlightArrivalAirportId(departureAirportId, arrivalAirportId, pageable)
                .map(FlightScheduleMapper::toResponse);
    }

    @Override
    public List<FlightScheduleResponse> getByOperatingDay(DayOfWeek day) {
        if(day == null){
            throw new IllegalArgumentException("Operating day cannot be null");
        }

        return flightScheduleRepository.findByOperatingDaysContaining(day).stream()
                .filter(schedule -> Boolean.TRUE.equals(schedule.getActive()))
                .map(FlightScheduleMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public FlightScheduleResponse updateSchedule(Long scheduleId, FlightScheduleRequest request, Long airlineId) {
        if(airlineId == null){
            throw new IllegalArgumentException("Airline id cannot be null");
        }

        FlightSchedule schedule = flightScheduleHelper.findByIdAndAirline(scheduleId, airlineId);
        flightScheduleHelper.validateUpdate(schedule, request, airlineId);

        FlightScheduleMapper.updateEntity(schedule, request);
        FlightSchedule updated = flightScheduleRepository.save(schedule);
        return FlightScheduleMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void deleteSchedule(Long scheduleId, Long airlineId) {
        FlightSchedule schedule = flightScheduleHelper.findByIdAndAirline(scheduleId, airlineId);
        flightScheduleHelper.validateCanDelete(schedule);
        flightScheduleRepository.delete(schedule);
    }
}
