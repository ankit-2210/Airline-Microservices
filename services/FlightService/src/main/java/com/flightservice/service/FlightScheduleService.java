package com.flightservice.service;

import com.airlineportal.payload.request.Flight.FlightScheduleRequest;
import com.airlineportal.payload.response.Flight.FlightScheduleResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.DayOfWeek;
import java.util.*;

public interface FlightScheduleService {

    FlightScheduleResponse createSchedule(Long flightId, FlightScheduleRequest request, Long airlineId);
    FlightScheduleResponse getById(Long scheduleId);

    Page<FlightScheduleResponse> getByFlight(Long flightId, Pageable pageable);
    Page<FlightScheduleResponse> getByAirline(Long airlineId, Pageable pageable);
    Page<FlightScheduleResponse> getByRoute(Long departureAirportId, Long arrivalAirportId, Pageable pageable);

    List<FlightScheduleResponse> getByOperatingDay(DayOfWeek day);

    FlightScheduleResponse updateSchedule(Long scheduleId, FlightScheduleRequest request, Long airlineId);

    void deleteSchedule(Long scheduleId, Long airlineId);



}
