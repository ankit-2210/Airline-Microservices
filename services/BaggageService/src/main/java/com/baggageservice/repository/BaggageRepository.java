package com.baggageservice.repository;

import com.airlineportal.utils.Baggage.BaggageStatus;
import com.baggageservice.model.Baggage;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface BaggageRepository extends JpaRepository<Baggage, Long> {
    List<Baggage> findByBookingId(Long bookingId);
    List<Baggage> findByPassengerId(Long passengerId);
    List<Baggage> findByFlightInstanceId(Long flightInstanceId);

    Page<Baggage> findByBookingId(Long bookingId, Pageable pageable);
    Page<Baggage> findByPassengerId(Long passengerId, Pageable pageable);
    Page<Baggage> findByFlightInstanceId(Long flightInstanceId, Pageable pageable);

    List<Baggage> findByStatus(BaggageStatus status);

    boolean existsByBookingIdAndPassengerIdAndStatusNot(Long bookingId, Long passengerId, BaggageStatus status);



}
