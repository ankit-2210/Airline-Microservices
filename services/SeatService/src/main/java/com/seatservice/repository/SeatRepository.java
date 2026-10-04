package com.seatservice.repository;

import com.airlineportal.utils.Seat.SeatStatus;
import com.seatservice.model.Seat;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long>{
    Optional<Seat> findByFlightInstanceIdAndSeatNumber(Long flightInstanceId, String seatNumber);

    List<Seat> findByFlightInstanceId(Long flightInstanceId);
    List<Seat> findByFlightInstanceIdAndSeatStatus(Long flightInstanceId, SeatStatus seatStatus);
    List<Seat> findByBookingId(Long bookingId);

    boolean existsByFlightInstanceIdAndSeatNumber(Long flightInstanceId, String seatNumber);

    @Modifying
    @Query("""
            update Seat s
                set s.seatStatus = :bookedStatus,
                    s.bookingId = :bookingId
                where s.flightInstanceId = :flightInstanceId
                    and s.seatNumber = :seatNumber
                    and s.seatStatus = :availableStatus
            """)
    int bookSeat(@Param("flightInstanceId") Long flightInstanceId, @Param("seatNumber") String seatNumber,
                 @Param("bookingId") Long bookingId, @Param("bookedStatus") SeatStatus bookedStatus,
                 @Param("availableStatus") SeatStatus availableStatus);

    @Modifying
    @Query("""
           update Seat s
                set s.seatStatus = :availableStatus,
                    s.bookingId = null
                where s.flightInstanceId = :flightInstanceId
                    and s.seatNumber = :seatNumber
                    and s.seatStatus = :bookedStatus
            """)
    int releaseSeat(@Param("flightInstanceId") Long flightInstanceId, @Param("seatNumber") String seatNumber,
                    @Param("availableStatus") SeatStatus availableStatus, @Param("bookedStatus") SeatStatus bookedStatus);


}
