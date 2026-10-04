package com.seatservice.mapper;

import com.airlineportal.payload.request.Seat.SeatCreateRequest;
import com.airlineportal.payload.response.Seat.SeatResponse;
import com.seatservice.model.Seat;

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

}
