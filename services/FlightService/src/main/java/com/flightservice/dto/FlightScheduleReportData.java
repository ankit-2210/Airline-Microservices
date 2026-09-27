package com.flightservice.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class FlightScheduleReportData {
    private Long id;
    private Long flightId;
    private String flightNumber;

    private LocalTime departureTime;
    private LocalTime arrivalTime;

    private LocalDate startDate;
    private LocalDate endDate;

    private String operatingDays;
    private Boolean active;

    public FlightScheduleReportData(
            Long id,
            Long flightId,
            String flightNumber,
            LocalTime departureTime,
            LocalTime arrivalTime,
            LocalDate startDate,
            LocalDate endDate,
            String operatingDays,
            Boolean active){

        this.id = id;
        this.flightId = flightId;
        this.flightNumber = flightNumber;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.startDate = startDate;
        this.endDate = endDate;
        this.operatingDays = operatingDays;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public Long getFlightId() {
        return flightId;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public LocalTime getDepartureTime() {
        return departureTime;
    }

    public LocalTime getArrivalTime() {
        return arrivalTime;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public String getOperatingDays() {
        return operatingDays;
    }

    public Boolean getActive() {
        return active;
    }

}
