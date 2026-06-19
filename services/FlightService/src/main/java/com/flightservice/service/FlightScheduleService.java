package com.flightservice.service;

import com.microservices.payload.request.Flight.FlightScheduleRequest;
import com.microservices.payload.response.Flight.FlightScheduleResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.DayOfWeek;
import java.util.*;

public interface FlightScheduleService {
    FlightScheduleResponse createFlightSchedule(Long airlineId, FlightScheduleRequest flightScheduleRequest);

    FlightScheduleResponse getFlightScheduleById(Long id);

    Page<FlightScheduleResponse> getFlightScheduleByAirline(Long airlineId, Pageable pageable);
    Page<FlightScheduleResponse> getScheduleByFlightId(Long flightId, Pageable pageable);
    Page<FlightScheduleResponse> getScheduleByRoute(Long departureAirportId, Long arrivalAirportId, Pageable pageable);

    FlightScheduleResponse updateFlightSchedule(Long airlineId, Long id, FlightScheduleRequest flightScheduleRequest);
    FlightScheduleResponse changeActiveStatus(Long airlineId, Long id, Boolean active);

    List<FlightScheduleResponse> getScheduleByOperatingDay(DayOfWeek day);
    void generateFlightInstances(Long scheduleId);

    void deleteFlightSchedule(Long airlineId, Long id);

}
