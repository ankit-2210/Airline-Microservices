package com.flightservice.service.Impl;

import com.flightservice.repository.FlightRepository;
import com.flightservice.service.FlightReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FlightReportServiceImpl implements FlightReportService {
    private final FlightRepository flightRepository;


    @Override
    public byte[] generateFlightsPdf() {
        return new byte[0];
    }

    @Override
    public byte[] generateFlightSchedulesPdf() {
        return new byte[0];
    }

    @Override
    public byte[] generateFlightInstancesPdf() {
        return new byte[0];
    }
}
