package com.seatservice.mapper;

import com.airlineportal.payload.request.Seat.SeatCreateRequest;
import com.airlineportal.payload.response.Seat.SeatResponse;
import com.seatservice.dto.SeatReportData;
import com.seatservice.model.Seat;

import java.time.LocalDateTime;
import java.time.ZoneId;

public final class SeatMapper {
    private SeatMapper(){

    }

    public static Seat toEntity(SeatCreateRequest request) {

        return Seat.builder()
                .flightInstanceId(request.getFlightInstanceId())
                .seatNumber(request.getSeatNumber())
                .seatClass(request.getSeatClass())
                .build();

    }

    public static SeatResponse toResponse(Seat seat) {

        return SeatResponse.builder()
                .id(seat.getId())
                .flightInstanceId(seat.getFlightInstanceId())

                .seatNumber(seat.getSeatNumber())
                .seatClass(seat.getSeatClass())
                .seatStatus(seat.getSeatStatus())

                .bookingId(seat.getBookingId())
                .createdAt(seat.getCreatedAt())
                .updatedAt(seat.getUpdatedAt())
                .build();

    }

    private static final ZoneId REPORT_ZONE = ZoneId.of("Asia/Kolkata");

    public static SeatReportData toReportData(Seat seat) {
        if (seat == null)
            return null;

        return SeatReportData.builder()
                .id(seat.getId())
                .flightInstanceId(seat.getFlightInstanceId())

                .seatNumber(seat.getSeatNumber())

                .seatClass(seat.getSeatClass() != null ? seat.getSeatClass().name() : null)
                .seatStatus(seat.getSeatStatus() != null ? seat.getSeatStatus().name() : null)

                .bookingId(seat.getBookingId())
                .createdAt(seat.getCreatedAt() != null ? seat.getCreatedAt()
                        .atZone(REPORT_ZONE).toLocalDateTime().toString() : "-")
                .updatedAt( seat.getUpdatedAt() != null ? seat.getUpdatedAt()
                        .atZone(REPORT_ZONE).toLocalDateTime().toString() : "-")
                .build();

    }

}
