package com.checkinservice.service;

public interface CheckInReportService {

    byte[] generateCheckInsPdf();
    byte[] generateBookingCheckInsPdf(Long bookingId);
    byte[] generateFlightInstanceCheckInsPdf(Long flightInstanceId);



}
