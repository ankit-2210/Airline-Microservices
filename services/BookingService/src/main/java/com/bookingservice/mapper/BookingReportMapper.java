package com.bookingservice.mapper;

import com.bookingservice.dto.BookingReportData;
import com.bookingservice.dto.PassengerReportData;
import com.bookingservice.model.Booking;
import com.bookingservice.model.Passenger;

public final class BookingReportMapper {
    private BookingReportMapper(){

    }

    public static BookingReportData toBookingReportData(Booking booking){
        if(booking == null)
            return null;

        return new BookingReportData(
                booking.getId(),
                booking.getPnr(),
                booking.getUserId(),
                booking.getFlightId(),
                booking.getFlightInstanceId(),
                booking.getTotalAmount(),

                booking.getBookingStatus() == null ? null : booking.getBookingStatus().name(),
                booking.getPaymentStatus() == null ? null : booking.getPaymentStatus().name(),

                booking.getBookedAt(),
                booking.getConfirmedAt(),
                booking.getCancelledAt()
        );
    }

    public static PassengerReportData toPassengerReportData(Passenger passenger){
        if(passenger == null)
            return null;

        Booking booking = passenger.getBooking();
        return new PassengerReportData(
                passenger.getId(),

                booking == null ? null : booking.getId(),
                booking == null ? null : booking.getPnr(),

                passenger.getFirstName(),
                passenger.getLastName(),
                passenger.getGender(),
                passenger.getPassportNumber(),
                passenger.getSeatNumber()
        );
    }



}
