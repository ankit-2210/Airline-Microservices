package com.airlineservice.repository;

import com.airlineportal.utils.Airline.AircraftStatus;
import com.airlineportal.utils.Airline.AirlineStatus;
import com.airlineservice.model.Airline;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AirlineRepository extends JpaRepository<Airline, Long> {

    // Owner
    Optional<Airline> findByOwnerId(Long ownerId);
    boolean existsByOwnerId(Long ownerId);

    // IATA
    Optional<Airline> findByIataCode(String iataCode);
    boolean existsByIataCode(String iataCode);
    boolean existsByIataCodeAndIdNot(String iataCode, Long id);

    // ICAO
    Optional<Airline> findByIcaoCode(String icaoCode);
    boolean existsByIcaoCode(String icaoCode);
    boolean existsByIcaoCodeAndIdNot(String icanCode, Long id);

    // Status
    Page<Airline> findByAirlineStatus(AircraftStatus aircraftStatus, Pageable pageable);
    List<Airline> findByAirlineStatus(AirlineStatus airlineStatus);

    // Country
    Page<Airline> findByCountryIgnoreCase(String country, Pageable pageable);

    // Name
    Page<Airline> findByNameContainingIgnoreCase(String name, Pageable pageable);

    // Search
    Page<Airline> findByNameContainingIgnoreCaseOrIataCodeContainingIgnoreCaseOrIcaoCodeContainingIgnoreCase(String name, String iataCode, String icaoCode, Pageable pageable);

    // Country + States
    Page<Airline> findByCountryIgnoreCaseAndAirlineStatus(String country, AirlineStatus airlineStatus, Pageable pageable);


    // Statistics
    long countByAirlineStatus(AirlineStatus airlineStatus);
    long countByCountryIgnoreCase(String country);

}
