package com.flightservice.service.Impl;

import com.flightservice.service.FlightScheduleService;
import com.microservices.payload.request.Flight.FlightScheduleRequest;
import com.microservices.payload.response.Flight.FlightScheduleResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.DayOfWeek;
import java.util.List;

public class FlightScheduleServiceImpl implements FlightScheduleService {

    @Override
    public FlightScheduleResponse createFlightSchedule(Long airlineId, FlightScheduleRequest flightScheduleRequest) {
        return null;
    }

    @Override
    public FlightScheduleResponse getFlightScheduleById(Long id) {
        return null;
    }

    @Override
    public Page<FlightScheduleResponse> getFlightScheduleByAirline(Long airlineId, Pageable pageable) {
        return null;
    }

    @Override
    public Page<FlightScheduleResponse> getScheduleByFlightId(Long flightId, Pageable pageable) {
        return null;
    }

    @Override
    public Page<FlightScheduleResponse> getScheduleByRoute(Long departureAirportId, Long arrivalAirportId, Pageable pageable) {
        return null;
    }

    @Override
    public FlightScheduleResponse updateFlightSchedule(Long airlineId, Long id, FlightScheduleRequest flightScheduleRequest) {
        return null;
    }


    @Override
    public FlightScheduleResponse changeActiveStatus(Long airlineId, Long id, Boolean active) {
        return null;
    }

    @Override
    public List<FlightScheduleResponse> getScheduleByOperatingDay(DayOfWeek day) {
        return List.of();
    }

    @Override
    public void generateFlightInstances(Long scheduleId) {

    }

    @Override
    public void deleteFlightSchedule(Long airlineId, Long id) {

    }

}
