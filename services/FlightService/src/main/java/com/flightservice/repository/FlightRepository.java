package com.flightservice.repository;

import com.flightservice.model.Flight;
import com.microservices.utils.Flight.FlightStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Optional;

public interface FlightRepository extends JpaRepository<Flight, Long> {
    Page<Flight> findByAirlineId(Long airlineId, Pageable pageable);

    Page<Flight> findByAirlineIdAndDepartureAirportIdAndArrivalAirportId(Long airlineId, Long departureAirportId, Long arrivalAirportId, Pageable pageable);

    boolean existsByFlightNumber(String flightNumber);
    boolean existsByFlightNumberAndIdNot(String flightNumber, Long id);

    Optional<Flight> findByAirlineIdAndId(Long airlineId, Long id);

    Page<Flight> findByFlightStatus(FlightStatus flightStatus, Pageable pageable);
    Page<Flight> findByDepartureAirportId(Long airlineId, Pageable pageable);
    Page<Flight> findByArrivalAirportId(Long airlineId, Pageable pageable);
    Page<Flight> findByDepartureAirportIdAndArrivalAirportId(Long departureAirportId, Long arrivalAirportId, Pageable pageable);

    @Query("""
       SELECT f
       FROM Flight f
       WHERE f.airlineId = :airlineId
       AND f.scheduledDeparture > CURRENT_TIMESTAMP
       """)
    Page<Flight> findUpcomingFlight(Long airlineId, Pageable pageable);

    @Query("""
       SELECT f
       FROM Flight f
       WHERE f.departureAirportId = :departureAirportId
       AND f.arrivalAirportId = :arrivalAirportId
       AND FUNCTION('DATE', f.scheduledDeparture) = :departureDate
       """)
    Page<Flight> searchFlightsByDate(Long departureAirportId, Long arrivalAirportId, LocalDate departureDate, Pageable pageable);
}
