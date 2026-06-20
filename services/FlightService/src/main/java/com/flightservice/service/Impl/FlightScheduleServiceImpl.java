package com.flightservice.service.Impl;

import com.flightservice.mapper.FlightScheduleMapper;
import com.flightservice.model.Flight;
import com.flightservice.model.FlightInstance;
import com.flightservice.model.FlightSchedule;
import com.flightservice.repository.FlightInstanceRepository;
import com.flightservice.repository.FlightRepository;
import com.flightservice.repository.FlightScheduleRepository;
import com.flightservice.service.FlightScheduleService;
import com.microservices.exception.ResourceNotFoundException;
import com.microservices.payload.request.Flight.FlightScheduleRequest;
import com.microservices.payload.response.Flight.FlightScheduleResponse;
import com.microservices.utils.Flight.FlightStatus;
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
    private final FlightScheduleRepository scheduleRepository;
    private final FlightRepository flightRepository;
    private final FlightInstanceRepository flightInstanceRepository;

    private Flight findFlight(Long id){
        return flightRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Flight not found with id: " + id));
    }

    @Transactional
    @Override
    public FlightScheduleResponse createFlightSchedule(Long airlineId, FlightScheduleRequest flightScheduleRequest) {
        Flight flight = findFlight(flightScheduleRequest.getFlightId());
        if(!flight.getAirlineId().equals(airlineId)){
            throw new ResourceNotFoundException("Flight not belong to airline");
        }

        FlightSchedule schedule = FlightScheduleMapper.toEntity(flightScheduleRequest);
        schedule.setFlight(flight);
        return FlightScheduleMapper.toResponse(scheduleRepository.save(schedule));
    }

    @Override
    public FlightScheduleResponse getFlightScheduleById(Long id) {
        return FlightScheduleMapper.toResponse(scheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Flight schedule not found")));
    }

    @Override
    public Page<FlightScheduleResponse> getFlightScheduleByAirline(Long airlineId, Pageable pageable) {
        return scheduleRepository.findByFlightAirlineId(airlineId, pageable)
                .map(FlightScheduleMapper::toResponse);
    }

    @Override
    public Page<FlightScheduleResponse> getScheduleByFlightId(Long flightId, Pageable pageable) {
        return scheduleRepository.findByFlightId(flightId, pageable)
                .map(FlightScheduleMapper::toResponse);
    }

    @Override
    public Page<FlightScheduleResponse> getScheduleByRoute(Long departureAirportId, Long arrivalAirportId, Pageable pageable) {
        return scheduleRepository.findByFlightDepartureAirportIdAndFlightArrivalAirportId(departureAirportId, arrivalAirportId, pageable)
                .map(FlightScheduleMapper::toResponse);
    }

    @Transactional
    @Override
    public FlightScheduleResponse updateFlightSchedule(Long airlineId, Long id, FlightScheduleRequest flightScheduleRequest) {
        FlightSchedule schedule = scheduleRepository.findByIdAndFlightAirlineId(id, airlineId)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found"));
        FlightScheduleMapper.updateEntity(schedule, flightScheduleRequest);
        return FlightScheduleMapper.toResponse(scheduleRepository.save(schedule));
    }

    @Transactional
    @Override
    public FlightScheduleResponse changeActiveStatus(Long airlineId, Long id, Boolean active) {
        FlightSchedule schedule = scheduleRepository.findByIdAndFlightAirlineId(id, airlineId)
                .orElseThrow(() -> new ResourceNotFoundException("Not found by id"));
        schedule.setActive(active);
        return FlightScheduleMapper.toResponse(scheduleRepository.save(schedule));
    }

    @Override
    public List<FlightScheduleResponse> getScheduleByOperatingDay(DayOfWeek day) {
        return scheduleRepository.findByOperatingDaysContaining(day).stream()
                .map(FlightScheduleMapper::toResponse)
                .toList();
    }

    @Transactional
    @Override
    public void generateFlightInstances(Long scheduleId) {
        FlightSchedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found"));
        LocalDate date = schedule.getStartDate();
        while(!date.isAfter(schedule.getEndDate())){
            if(schedule.getOperatingDays().contains(date.getDayOfWeek())){
                boolean exists = flightInstanceRepository.existsByScheduleIdAndDepartureDateTime(scheduleId, schedule.getDepartureDateTime(date));
                if(!exists){
                    FlightInstance instance = FlightInstance.builder()
                            .flight(schedule.getFlight())
                            .schedule(schedule)

                            .departureDateTime(schedule.getDepartureDateTime(date))
                            .arrivalDateTime(schedule.getArrivalDateTime(date))

                            .totalSeats(180)
                            .availableSeats(180)

                            .flightStatus(FlightStatus.SCHEDULED)

                            .active(true)
                            .build();

                    flightInstanceRepository.save(instance);
                }
            }

            date = date.plusDays(1);
        }
    }


    @Transactional
    @Override
    public void deleteFlightSchedule(Long airlineId, Long id) {
        scheduleRepository.delete(scheduleRepository.findByIdAndFlightAirlineId(id, airlineId)
                .orElseThrow(()-> new ResourceNotFoundException("Not found by id")));
    }

}
