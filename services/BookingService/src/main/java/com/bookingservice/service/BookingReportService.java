package com.bookingservice.service;

public interface BookingReportService {

    byte[] generateBookingsPdf();

    byte[] generatePassengersPdf();


}
