package com.locationservice.repository;

import com.locationservice.model.Airport;
import com.microservices.payload.response.Airport.AirportResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface AirportRepository extends JpaRepository<Airport, Long> {
    Optional<Airport> findByIataCode(String iataCode);
    List<Airport> findByCityId(Long cityId);
    boolean existsByIataCode(String iataCode);
    List<Airport> findByNameContainingIgnoreCase(String name);

}
