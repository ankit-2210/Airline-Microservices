package com.bookingservice.mapper;

import com.airlineportal.payload.request.Booking.BookingRequest;
import com.airlineportal.payload.response.Booking.BookingResponse;
import com.airlineportal.payload.response.Booking.PassengerResponse;
import com.bookingservice.model.Booking;

import java.util.stream.Collectors;
import java.util.*;

public final class BookingMapper {
    private BookingMapper(){

    }

    public static Booking toEntity(BookingRequest request, String pnr, Long flightId){
        if(request == null)
            return null;

        Booking booking = Booking.builder()
                .pnr(pnr)
                .userId(request.getUserId())
                .flightId(flightId)
                .flightInstanceId(request.getFlightInstanceId())
                .build();

        if(request.getPassengers() != null){
            request.getPassengers().stream()
                    .map(PassengerMapper::toEntity)
                    .forEach(booking::addPassenger);
        }

        return booking;
    }

    public static BookingResponse toResponse(Booking booking){
        if(booking == null)
            return null;

        List<PassengerResponse> passengers = booking.getPassengers() == null ? List.of()
                : booking.getPassengers().stream()
                .map(PassengerMapper::toResponse)
                .toList();

        return BookingResponse.builder()
                .id(booking.getId())
                .pnr(booking.getPnr())
                .userId(booking.getUserId())
                .flightId(booking.getFlightId())
                .totalAmount(booking.getTotalAmount())
                .bookingStatus(booking.getBookingStatus() == null ? null : booking.getBookingStatus().name())
                .paymentStatus(booking.getPaymentStatus() == null ? null : booking.getPaymentStatus().name())
                .bookedAt(booking.getBookedAt())
                .confirmedAt(booking.getConfirmedAt())
                .cancelledAt(booking.getCancelledAt())
                .cancellationReason(booking.getCancellationReason())
                .passengers(passengers)
                .build();
    }


}
