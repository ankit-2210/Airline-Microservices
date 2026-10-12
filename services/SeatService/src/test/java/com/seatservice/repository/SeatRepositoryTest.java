package com.seatservice.repository;

import com.airlineportal.utils.Seat.SeatClass;
import com.airlineportal.utils.Seat.SeatStatus;
import com.seatservice.model.Seat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class SeatRepositoryTest {
    @Autowired
    private SeatRepository seatRepository;

    private Seat seat;

    @BeforeEach
    void setUp(){
        seatRepository.deleteAll();

        seat = seatRepository.save(Seat.builder()
                .flightInstanceId(100L)
                .seatNumber("12A")
                .seatClass(SeatClass.ECONOMY)
                .seatStatus(SeatStatus.AVAILABLE)
                .build());
    }

    @Test
    void findByFlightInstanceIdAndSeatNumber_shouldFindSeat() {
        var result = seatRepository.findByFlightInstanceIdAndSeatNumber(100L, "12A");

        assertTrue(result.isPresent());
        assertEquals("12A", result.get().getSeatNumber());
    }

    @Test
    void findByFlightInstanceId_shouldReturnMatchingSeats(){
        seatRepository.save(Seat.builder()
                .flightInstanceId(100L)
                .seatNumber("12B")
                .seatClass(SeatClass.ECONOMY)
                .seatStatus(SeatStatus.AVAILABLE)
                .build());

        seatRepository.save(Seat.builder()
                .flightInstanceId(200L)
                .seatNumber("12A")
                .seatClass(SeatClass.BUSINESS)
                .seatStatus(SeatStatus.AVAILABLE)
                .build());

        List<Seat> result = seatRepository.findByFlightInstanceId(100L);

        assertEquals(2, result.size());
        assertTrue(result.stream()
                .allMatch(s -> s.getFlightInstanceId().equals(100L)));
    }


    @Test
    void findByFlightInstanceIdAndSeatStatus_shouldFilterByStatus() {
        seatRepository.save(Seat.builder()
                .flightInstanceId(100L)
                .seatNumber("12B")
                .seatClass(SeatClass.ECONOMY)
                .seatStatus(SeatStatus.BOOKED)
                .bookingId(500L)
                .build());

        List<Seat> result = seatRepository.findByFlightInstanceIdAndSeatStatus(100L, SeatStatus.AVAILABLE);
        assertEquals(1, result.size());
        assertEquals("12A", result.getFirst().getSeatNumber());

    }

    @Test
    void findByBookingId_shouldReturnBookedSeats() {
        seatRepository.save(Seat.builder()
                .flightInstanceId(100L)
                .seatNumber("12B")
                .seatClass(SeatClass.ECONOMY)
                .seatStatus(SeatStatus.BOOKED)
                .bookingId(500L)
                .build());

        List<Seat> result = seatRepository.findByBookingId(500L);

        assertEquals(1, result.size());
        assertEquals("12B", result.getFirst().getSeatNumber());
    }

    @Test
    void existsByFlightInstanceIdAndSeatNumber_shouldReturnTrue() {
        assertTrue(
                seatRepository.existsByFlightInstanceIdAndSeatNumber(
                        100L, "12A"
                )
        );

        assertFalse(
                seatRepository.existsByFlightInstanceIdAndSeatNumber(
                        100L, "99A"
                )
        );
    }

    @Test
    void bookSeat_shouldUpdateOnlyAvailableSeat() {
        int updated = seatRepository.bookSeat(
                100L,
                "12A",
                500L,
                SeatStatus.BOOKED,
                SeatStatus.AVAILABLE
        );

        assertEquals(1, updated);

        Seat saved = seatRepository.findByFlightInstanceIdAndSeatNumber(100L, "12A")
                .orElseThrow();

        assertEquals(SeatStatus.BOOKED, saved.getSeatStatus());
        assertEquals(500L, saved.getBookingId());

        // A second booking must not update an already-booked seat.
        int secondUpdate = seatRepository.bookSeat(
                100L,
                "12A",
                501L,
                SeatStatus.BOOKED,
                SeatStatus.AVAILABLE
        );

        assertEquals(0, secondUpdate);
    }

    @Test
    void releaseSeat_shouldClearBookingAndMakeSeatAvailable() {
        seatRepository.bookSeat(
                100L,
                "12A",
                500L,
                SeatStatus.BOOKED,
                SeatStatus.AVAILABLE
        );

        int updated = seatRepository.releaseSeat(
            100L,
            "12A",
            SeatStatus.AVAILABLE,
            SeatStatus.BOOKED
        );

        assertEquals(1, updated);
        Seat saved = seatRepository.findByFlightInstanceIdAndSeatNumber(100L, "12A")
                .orElseThrow();

        assertEquals(SeatStatus.AVAILABLE, saved.getSeatStatus());
        assertNull(saved.getBookingId());

    }


}
