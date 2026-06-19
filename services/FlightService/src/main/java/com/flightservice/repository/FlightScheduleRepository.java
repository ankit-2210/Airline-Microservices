package com.flightservice.repository;

import com.flightservice.model.FlightSchedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.*;

@Repository
public interface FlightScheduleRepository extends JpaRepository<FlightSchedule, Long> {
    Page<FlightSchedule> findByFlightAirlineId(Long airlineId, Pageable pageable);
    Page<FlightSchedule> findByFlightId(Long flightId, Pageable pageable);

    Page<FlightSchedule> findByFlightDepartureAirportIdAndFlightArrivalAirportId(Long departureAirportId, Long arrivalAirportId, Pageable pageable);
    List<FlightSchedule> findByOperatingDaysContaining(DayOfWeek day);

    Optional<FlightSchedule> findByIdAndFlightAirlineId(Long id, Long airlineId);



}
