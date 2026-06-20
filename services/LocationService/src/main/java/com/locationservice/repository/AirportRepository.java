package com.locationservice.repository;

import com.locationservice.model.Airport;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface AirportRepository extends JpaRepository<Airport, Long> {
    Optional<Airport> findByIataCode(String iataCode);
    Optional<Airport> findByIataCodeIgnoreCase(String iataCode);

    boolean existsByIataCode(String iataCode);
    boolean existsByIataCodeAndIdNot(String iataCode, Long id);

    List<Airport> findByCityId(Long cityId);

    Page<Airport> findByCityId(Long cityId, Pageable pageable);
    Page<Airport> findByCityCountryCodeIgnoreCase(String countryCode, Pageable pageable);

    @Query("""
            SELECT a
            FROM Airport a
            WHERE LOWER(a.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(a.iataCode) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(a.city.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(a.city.countryName) LIKE LOWER(CONCAT('%', :keyword, '%'))
                
            """)
    Page<Airport> search(String keyword, Pageable pageable);

    @Query("""
            SELECT a
            FROM Airport a
            ORDER BY a.name ASC
            """)
    List<Airport> findDropdown();

}
