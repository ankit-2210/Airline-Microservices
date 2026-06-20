package com.airlineservice.repository;

import com.airlineservice.model.Aircraft;
import com.airlineservice.model.Airline;
import com.microservices.utils.Airline.AircraftStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface AirlineRepository extends JpaRepository<Airline, Long> {

    Optional<Airline> findByOwnerId(Long ownerId);

    boolean existsByIataCode(String iataCode);
    boolean existsByIcaoCode(String icaoCode);

    boolean existsByIataCodeAndIdNot(String iataCode, Long id);
    boolean existsByIcaoCodeAndIdNot(String icanCode, Long id);

    Page<Airline> findByAirlineStatus(AircraftStatus aircraftStatus, Pageable pageable);
    Page<Airline> findByCountry(String country, Pageable pageable);
    Page<Airline> findByNameContainingIgnoreCase(String name, Pageable pageable);
    List<Airline> findByAirlineStatus(AircraftStatus aircraftStatus);

    Page<Airline> findByOwnerId(Long ownerId, Pageable pageable);

}
