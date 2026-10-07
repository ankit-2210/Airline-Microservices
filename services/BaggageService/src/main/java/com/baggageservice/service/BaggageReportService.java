package com.baggageservice.service;

public interface BaggageReportService {

    byte[] generateBaggagePdf();
    byte[] generateBookingBaggagePdf(Long bookingId);
    byte[] generatePassengerBaggagePdf(Long passengerId);
    byte[] generateFlightInstanceBaggagePdf(Long flightInstanceId);


}
