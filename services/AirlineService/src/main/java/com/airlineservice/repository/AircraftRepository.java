package com.airlineservice.repository;

import com.airlineservice.model.Aircraft;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface AircraftRepository extends JpaRepository<Aircraft, Long> {
    Optional<Aircraft> findByCode(String code);
    boolean existsByCode(String code);

    boolean existsByCodeAndIdNot(String code, Long id);

    List<Aircraft> findByAirlineId(Long airlineId);
    Page<Aircraft> findByAirlineOwnerId(Long ownerId, Pageable pageable);

    List<Aircraft> findByCurrentAirportId(Long airportId);


}
