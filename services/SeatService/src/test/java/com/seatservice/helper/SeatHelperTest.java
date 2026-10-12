package com.seatservice.helper;


import com.airlineportal.exception.ResourceNotFoundException;
import com.airlineportal.payload.request.Seat.SeatCreateRequest;
import com.airlineportal.utils.Seat.SeatClass;
import com.airlineportal.utils.Seat.SeatStatus;
import com.seatservice.model.Seat;
import com.seatservice.repository.SeatRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SeatHelperTest {
    @Mock
    private SeatRepository seatRepository;

    @InjectMocks
    private SeatHelper seatHelper;

    private Seat seat;

    @BeforeEach
    void setUp() {
        seat = Seat.builder()
                .id(1L)
                .flightInstanceId(100L)
                .seatNumber("12A")
                .seatClass(SeatClass.ECONOMY)
                .seatStatus(SeatStatus.AVAILABLE)
                .build();
    }

    // findById
    @Test
    void findById_shouldReturnSeatWhenFound() {
        when(seatRepository.findById(1L))
                .thenReturn(Optional.of(seat));

        Seat result = seatHelper.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("12A", result.getSeatNumber());

        verify(seatRepository).findById(1L);
    }

    @Test
    void findById_shouldThrowWhenSeatIdIsNull() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> seatHelper.findById(null)
        );

        assertEquals("Seat id cannot be null", exception.getMessage());
        verifyNoInteractions(seatRepository);
    }

    @Test
    void findById_shouldThrowWhenSeatDoesNotExist() {
        when(seatRepository.findById(99L))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> seatHelper.findById(99L)
        );

        assertEquals("Seat not found with id: 99", exception.getMessage());
    }

    // findByInstanceAndNumber
    @Test
    void findByInstanceAndNumber_shouldReturnSeat() {
        when(seatRepository.findByFlightInstanceIdAndSeatNumber(
                100L, "12A"
        )).thenReturn(Optional.of(seat));

        Seat result = seatHelper.findByInstanceAndNumber(100L, "12a");

        assertNotNull(result);
        assertEquals("12A", result.getSeatNumber());

        verify(seatRepository).findByFlightInstanceIdAndSeatNumber(100L, "12A");
    }

    @Test
    void findByInstanceAndNumber_shouldNormalizeSeatNumber() {
        when(seatRepository.findByFlightInstanceIdAndSeatNumber(
                100L, "12A"
        )).thenReturn(Optional.of(seat));

        Seat result = seatHelper.findByInstanceAndNumber(100L, " 12a ");

        assertNotNull(result);
        verify(seatRepository).findByFlightInstanceIdAndSeatNumber(100L, "12A");
    }

    @Test
    void findByInstanceAndNumber_shouldThrowWhenSeatDoesNotExist() {
        when(seatRepository.findByFlightInstanceIdAndSeatNumber(
                100L, "99A"
        )).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> seatHelper.findByInstanceAndNumber(100L, "99a")
        );

        assertEquals("Seat not found: 99a", exception.getMessage());
    }

    // validateCreate
    @Test
    void validateCreate_shouldPassForValidUniqueSeat() {
        SeatCreateRequest request = mock(SeatCreateRequest.class);

        when(request.getFlightInstanceId()).thenReturn(100L);
        when(request.getSeatNumber()).thenReturn("12a");
        when(request.getSeatClass()).thenReturn(SeatClass.ECONOMY);

        when(seatRepository.existsByFlightInstanceIdAndSeatNumber(
                100L, "12A"
        )).thenReturn(false);

        assertDoesNotThrow(() -> seatHelper.validateCreate(request));
        verify(seatRepository).existsByFlightInstanceIdAndSeatNumber(100L, "12A");
    }

    @Test
    void validateCreate_shouldThrowWhenRequestIsNull() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> seatHelper.validateCreate(null)
        );

        assertEquals("Seat request cannot be null", exception.getMessage());
        verifyNoInteractions(seatRepository);
    }

    @Test
    void validateCreate_shouldThrowWhenFlightInstanceIdIsNull() {
        SeatCreateRequest request = mock(SeatCreateRequest.class);
        when(request.getFlightInstanceId())
                .thenReturn(null);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> seatHelper.validateCreate(request)
        );

        assertEquals("Flight instance id cannot be null", exception.getMessage());
        verifyNoInteractions(seatRepository);
    }

    @Test
    void validateCreate_shouldThrowWhenSeatNumberIsNull() {
        SeatCreateRequest request = mock(SeatCreateRequest.class);
        when(request.getFlightInstanceId()).thenReturn(100L);
        when(request.getSeatNumber()).thenReturn(null);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> seatHelper.validateCreate(request)
        );

        assertEquals("Seat number cannot be blank", exception.getMessage());
        verifyNoInteractions(seatRepository);
    }

    @Test
    void validateCreate_shouldThrowWhenSeatNumberIsBlank() {
        SeatCreateRequest request = mock(SeatCreateRequest.class);
        when(request.getFlightInstanceId()).thenReturn(100L);
        when(request.getSeatNumber()).thenReturn("   ");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> seatHelper.validateCreate(request)
        );

        assertEquals("Seat number cannot be blank", exception.getMessage());
        verifyNoInteractions(seatRepository);
    }

    @Test
    void validateCreate_shouldThrowWhenSeatClassIsNull() {
        SeatCreateRequest request = mock(SeatCreateRequest.class);

        when(request.getFlightInstanceId()).thenReturn(100L);
        when(request.getSeatNumber()).thenReturn("12A");
        when(request.getSeatClass()).thenReturn(null);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> seatHelper.validateCreate(request)
        );

        assertEquals("Seat class cannot be null", exception.getMessage());
        verifyNoInteractions(seatRepository);
    }

    @Test
    void validateCreate_shouldThrowWhenSeatAlreadyExists() {
        SeatCreateRequest request = mock(SeatCreateRequest.class);

        when(request.getFlightInstanceId()).thenReturn(100L);
        when(request.getSeatNumber()).thenReturn("12a");
        when(request.getSeatClass()).thenReturn(SeatClass.ECONOMY);

        when(seatRepository.existsByFlightInstanceIdAndSeatNumber(
                100L, "12A"
        )).thenReturn(true);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> seatHelper.validateCreate(request)
        );

        assertEquals("Seat already exists: 12A", exception.getMessage());
    }

    // validateAvailable
    @Test
    void validateAvailable_shouldPassForAvailableSeat() {
        seat.setSeatStatus(SeatStatus.AVAILABLE);

        assertDoesNotThrow(() -> seatHelper.validateAvailable(seat));
    }

    @Test
    void validateAvailable_shouldThrowForBookedSeat() {
        seat.setSeatStatus(SeatStatus.BOOKED);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> seatHelper.validateAvailable(seat)
        );

        assertEquals("Seat 12A is not available", exception.getMessage());
    }

    @Test
    void validateAvailable_shouldThrowForHeldSeat() {
        seat.setSeatStatus(SeatStatus.HELD);

        assertThrows(
                IllegalArgumentException.class,
                () -> seatHelper.validateAvailable(seat)
        );
    }

    @Test
    void validateAvailable_shouldThrowForBlockedSeat() {
        seat.setSeatStatus(SeatStatus.BLOCKED);

        assertThrows(
                IllegalArgumentException.class,
                () -> seatHelper.validateAvailable(seat)
        );
    }

    // normalizeSeatNumber
    @Test
    void normalizeSeatNumber_shouldTrimAndConvertToUppercase() {
        assertEquals("12A", seatHelper.normalizeSeatNumber(" 12a "));
    }

    @Test
    void normalizeSeatNumber_shouldReturnNullForNullInput() {
        assertNull(seatHelper.normalizeSeatNumber(null));
    }

    @Test
    void normalizeSeatNumber_shouldPreserveAlreadyNormalizedValue() {
        assertEquals("12A", seatHelper.normalizeSeatNumber("12A"));
    }




}
