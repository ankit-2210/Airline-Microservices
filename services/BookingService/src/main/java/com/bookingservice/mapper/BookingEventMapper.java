package com.bookingservice.mapper;

import com.airlineportal.event.booking.BookingCreatedEvent;
import com.bookingservice.model.Booking;

import java.time.LocalDateTime;

public final class BookingEventMapper {

    private BookingEventMapper() {

    }

    public static BookingCreatedEvent toEvent(Booking booking) {
        if (booking == null)
            return null;

        return BookingCreatedEvent.builder()
                .bookingId(booking.getId())
                .pnr(booking.getPnr())

                .userId(booking.getUserId())
                .flightId(booking.getFlightId())
                .flightInstanceId(booking.getFlightInstanceId())

                .totalAmount(booking.getTotalAmount())
                .passengerCount(booking.getPassengers().size())

                .occurredAt(LocalDateTime.now())
                .build();

    }

}
