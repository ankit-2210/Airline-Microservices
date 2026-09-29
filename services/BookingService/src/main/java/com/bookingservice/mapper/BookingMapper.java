package com.bookingservice.mapper;

import com.airlineportal.payload.request.Booking.BookingRequest;
import com.airlineportal.payload.response.Booking.BookingResponse;
import com.bookingservice.model.Booking;

import java.util.stream.Collectors;

public final class BookingMapper {
    private BookingMapper(){

    }

    public static Booking toEntity(BookingRequest request, String pnr){
        if(request == null)
            return null;

        Booking booking = Booking.builder()
                .pnr(pnr)
                .userId(request.getUserId())
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
                .passengers(booking.getPassengers().stream()
                        .map(PassengerMapper::toResponse)
                        .collect(Collectors.toList()))
                .build();
    }


}
