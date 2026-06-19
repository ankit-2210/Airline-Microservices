package com.flightservice.repository;

import com.flightservice.model.Flight;
import com.flightservice.model.FlightInstance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;

@Repository
public interface FlightInstanceRepository extends JpaRepository<FlightInstance, Long> {

    @Query("""
            select fi
            from FlightInstance fi
            where fi.flight.airlineId = :airlineId
            and (:departureAirportId is null
                    or fi.flight.departureAirportId = :departureAirportId)
            and (:arrivalAirportId is null
                    or fi.flight.arrivalAirportId = :arrivalAirportId)
            and (:flightId is null
                    or fi.flight.id = :flightId)
            and (:dayStart is null
                    or fi.departureDateTime >= :dayStart)
            and (:dayEnd is null
                    or fi.departureDateTime < :dayEnd)
            """)
    Page<FlightInstance> findByAirlineId(@Param("airlineId") Long airlineId,
                                         @Param("departureAirportId") Long departureAirportId,
                                         @Param("arrivalAirportId") Long arrivalAirportId,
                                         @Param("flightId") Long flightId,
                                         @Param("dayStart")LocalDateTime dayStart,
                                         @Param("dayEnd") LocalDateTime dayEnd,
                                         Pageable pageable);

    Optional<FlightInstance> findByIdAndFlightAirlineId(Long id, Long airlineId);
    Page<FlightInstance> findByFlightId(Long flightId, Pageable pageable);
    Page<FlightInstance> findByFlightAirlineId(Long airlineId, Pageable pageable);


}
