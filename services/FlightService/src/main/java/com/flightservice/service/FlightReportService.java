package com.flightservice.service;

public interface FlightReportService {
    byte[] generateFlightsPdf();

    byte[] generateFlightSchedulesPdf();

    byte[] generateFlightInstancesPdf();

}
