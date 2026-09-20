package com.bookingservice.repository;

import com.airlineportal.utils.Booking.BookingStatus;
import com.bookingservice.model.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long>{
    Optional<Booking> findByPnr(String pnr);

    boolean existsByPnr(String pnr);

    Page<Booking> findByUserId(Long userId, Pageable pageable);
    Page<Booking> findByFlightId(Long flightId, Pageable pageable);

    Page<Booking> findByBookingStatus(BookingStatus bookingStatus, Pageable pageable);
    Page<Booking> findByUserIdAndBookingStatus(Long userId, BookingStatus bookingStatus, Pageable pageable);

}
