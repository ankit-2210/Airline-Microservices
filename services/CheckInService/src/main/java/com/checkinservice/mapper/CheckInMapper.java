package com.checkinservice.mapper;

import com.airlineportal.payload.request.CheckIn.CheckInRequest;
import com.airlineportal.payload.response.Booking.BookingResponse;
import com.airlineportal.payload.response.CheckIn.CheckInResponse;
import com.checkinservice.dto.CheckInReportData;
import com.checkinservice.model.CheckIn;

public final class CheckInMapper {
    private CheckInMapper(){

    }

    public static CheckIn toEntity(CheckInRequest request, BookingResponse booking, String seatNumber){

        return CheckIn.builder()
                .bookingId(request.getBookingId())
                .passengerId(request.getPassengerId())
                .flightInstanceId(booking.getFlightInstanceId())

                .pnr(booking.getPnr())
                .seatNumber(seatNumber)
                .build();
    }

    public static CheckInResponse toResponse(CheckIn checkIn) {
        if(checkIn == null)
            return null;

        return CheckInResponse.builder()
                .id(checkIn.getId())
                .bookingId(checkIn.getBookingId())
                .passengerId(checkIn.getPassengerId())
                .flightInstanceId(checkIn.getFlightInstanceId())

                .pnr(checkIn.getPnr())
                .seatNumber(checkIn.getSeatNumber())

                .status(checkIn.getStatus())

                .checkedInAt(checkIn.getCheckedInAt())
                .createdAt(checkIn.getCreatedAt())
                .updatedAt(checkIn.getUpdatedAt())
                .build();

    }

    public static CheckInReportData toReportData(CheckIn checkIn) {

        return CheckInReportData.builder()
                .id(checkIn.getId())
                .bookingId(checkIn.getBookingId())
                .passengerId(checkIn.getPassengerId())
                .flightInstanceId(checkIn.getFlightInstanceId())

                .pnr(checkIn.getPnr())
                .seatNumber(checkIn.getSeatNumber())

                .status(checkIn.getStatus() != null ? checkIn.getStatus().name() : null)

                .checkedInAt(checkIn.getCheckedInAt())
                .createdAt(checkIn.getCreatedAt())
                .updatedAt(checkIn.getUpdatedAt())
                .build();
    }


}
