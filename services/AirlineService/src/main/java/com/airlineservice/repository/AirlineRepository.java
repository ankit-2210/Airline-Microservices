package com.airlineservice.repository;

import com.airlineservice.model.Airline;
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


}
