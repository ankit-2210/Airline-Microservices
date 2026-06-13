package com.locationservice.repository;

import com.locationservice.model.Airport;
import com.microservices.payload.response.Airport.AirportResponse;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;

import java.util.*;

public interface AirportRepository extends JpaRepository<Airport, Long> {
    Optional<Airport> findByIataCode(String iataCode);

    boolean existsByIataCode(String iataCode);

    boolean existsByIataCodeAndIdNot(String iataCode, Long id);

    List<Airport> findByCityId(Long cityId);

    Page<Airport> findByCityId(Long cityId, Pageable pageable);

    @Query("""
            SELECT a
            FROM Airport a
            WHERE LOWER(a.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(a.iataCode) LIKE LOWER(CONCAT('%', :keyword, '%'))
            """)
    Page<Airport> search(String keyword, Pageable pageable);
}
