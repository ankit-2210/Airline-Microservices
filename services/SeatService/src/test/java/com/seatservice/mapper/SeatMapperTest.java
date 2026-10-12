package com.seatservice.mapper;


import com.airlineportal.payload.request.Seat.SeatCreateRequest;
import com.airlineportal.utils.Seat.SeatClass;
import com.airlineportal.utils.Seat.SeatStatus;
import com.seatservice.dto.SeatReportData;
import com.seatservice.model.Seat;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class SeatMapperTest {
    @Test
    void toEntity_shouldMapCreateRequest() {
        SeatCreateRequest request = mock(SeatCreateRequest.class);

        when(request.getFlightInstanceId()).thenReturn(100L);
        when(request.getSeatNumber()).thenReturn("12A");
        when(request.getSeatClass()).thenReturn(SeatClass.ECONOMY);

        Seat result = SeatMapper.toEntity(request);

        assertNotNull(result);
        assertEquals(100L, result.getFlightInstanceId());
        assertEquals("12A", result.getSeatNumber());
        assertEquals(SeatClass.ECONOMY, result.getSeatClass());
        assertEquals(SeatStatus.AVAILABLE, result.getSeatStatus());

    }


    @Test
    void toResponse_shouldMapAllSeatFields() {
        Instant createdAt = Instant.parse("2026-01-10T10:00:00Z");
        Instant updatedAt = Instant.parse("2026-01-11T10:00:00Z");

        Seat seat = Seat.builder()
                .id(1L)
                .flightInstanceId(100L)
                .seatNumber("12A")
                .seatClass(SeatClass.BUSINESS)
                .seatStatus(SeatStatus.BOOKED)
                .bookingId(500L)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

        var result = SeatMapper.toResponse(seat);

        assertEquals(1L, result.getId());
        assertEquals(100L, result.getFlightInstanceId());
        assertEquals("12A", result.getSeatNumber());
        assertEquals(SeatClass.BUSINESS, result.getSeatClass());
        assertEquals(SeatStatus.BOOKED, result.getSeatStatus());
        assertEquals(500L, result.getBookingId());
        assertEquals(createdAt, result.getCreatedAt());
        assertEquals(updatedAt, result.getUpdatedAt());
    }


    @Test
    void toReportData_shouldMapFieldsAndConvertTimestampToKolkataTime() {
        Instant createdAt = Instant.parse("2026-01-10T10:00:00Z");
        Instant updatedAt = Instant.parse("2026-01-11T10:00:00Z");

        Seat seat = Seat.builder()
                .id(1L)
                .flightInstanceId(100L)
                .seatNumber("12A")
                .seatClass(SeatClass.BUSINESS)
                .seatStatus(SeatStatus.BOOKED)
                .bookingId(500L)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

        SeatReportData result = SeatMapper.toReportData(seat);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(100L, result.getFlightInstanceId());
        assertEquals("12A", result.getSeatNumber());
        assertEquals("ECONOMY", result.getSeatClass());
        assertEquals("BOOKED", result.getSeatStatus());
        assertEquals(500L, result.getBookingId());

        assertEquals(
                createdAt.atZone(ZoneId.of("Asia/Kolkata"))
                        .toLocalDateTime().toString(),
                result.getCreatedAt()
        );

        assertEquals(
                updatedAt.atZone(ZoneId.of("Asia/Kolkata"))
                        .toLocalDateTime().toString(),
                result.getUpdatedAt()
        );
    }

    @Test
    void toReportData_shouldReturnNullForNullSeat() {
        assertNull(SeatMapper.toReportData(null));
    }


    @Test
    void toReportData_shouldUseDashWhenTimestampsAreNull() {
        Seat seat = Seat.builder()
                .id(1L)
                .flightInstanceId(100L)
                .seatNumber("12A")
                .seatClass(null)
                .seatStatus(null)
                .createdAt(null)
                .updatedAt(null)
                .build();

        SeatReportData result = SeatMapper.toReportData(seat);

        assertNotNull(result);
        assertNull(result.getSeatClass());
        assertNull(result.getSeatStatus());
        assertEquals("-", result.getCreatedAt());
        assertEquals("-", result.getUpdatedAt());

    }





}
