package com.fareservice.repository;

import com.airlineportal.utils.Fare.FareClass;
import com.fareservice.model.Fare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FareRepository extends JpaRepository<Fare, Long> {
    List<Fare> findByFlightInstanceIdAndActiveTrue(Long flightInstanceId);

    Optional<Fare> findByFlightInstanceIdAndFareClassAndActiveTrue(Long flightInstanceId, FareClass fareClass);

    List<Fare> findByFlightIdAndActiveTrue(Long flightId);

    boolean existsByFlightInstanceIdAndFareClass(Long flightInstanceId, FareClass fareClass);


}
