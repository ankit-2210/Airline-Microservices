package com.checkinservice.repository;


import com.airlineportal.utils.CheckIn.CheckInStatus;
import com.checkinservice.model.CheckIn;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface CheckInRepository extends JpaRepository<CheckIn, Long> {
    Optional<CheckIn> findByBookingIdAndPassengerId(Long bookingId, Long passengerId);

    Page<CheckIn> findByBookingId(Long bookingId, Pageable pageable);
    Page<CheckIn> findByPassengerId(Long passengerId, Pageable pageable);
    Page<CheckIn> findByFlightInstanceId(Long flightInstanceId, Pageable pageable);

    // Reports
    List<CheckIn> findAllByBookingId(Long bookingId);
    List<CheckIn> findAllByFlightInstanceId(Long flightInstanceId);

    boolean existsByBookingIdAndPassengerId(Long bookingId, Long passengerId);
    boolean existsByBookingIdAndPassengerIdAndStatus(Long bookingId, Long passengerId, CheckInStatus status);



}
