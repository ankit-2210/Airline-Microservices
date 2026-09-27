package com.flightservice.dto;

import com.airlineportal.utils.Flight.FlightStatus;

import java.time.LocalDateTime;

public class FlightReportData {
    private Long id;
    private String flightNumber;

    private Long airlineId;
    private Long aircraftId;

    private Long departureAirportId;
    private Long arrivalAirportId;

    private LocalDateTime scheduledDeparture;
    private LocalDateTime scheduledArrival;

    private LocalDateTime actualDeparture;
    private LocalDateTime actualArrival;

    private FlightStatus flightStatus;
    private Boolean active;

    private Boolean delayed;
    private Long delayMinutes;
    private Long scheduledDurationMinutes;
    private Boolean operational;

    public FlightReportData(
            Long id,
            String flightNumber,
            Long airlineId,
            Long aircraftId,
            Long departureAirportId,
            Long arrivalAirportId,
            LocalDateTime scheduledDeparture,
            LocalDateTime scheduledArrival,
            LocalDateTime actualDeparture,
            LocalDateTime actualArrival,
            FlightStatus flightStatus,
            Boolean active,
            Boolean delayed,
            Long delayMinutes,
            Long scheduledDurationMinutes,
            Boolean operational){

        this.id = id;
        this.flightNumber = flightNumber;
        this.airlineId = airlineId;
        this.aircraftId = aircraftId;
        this.departureAirportId = departureAirportId;
        this.arrivalAirportId = arrivalAirportId;
        this.scheduledDeparture = scheduledDeparture;
        this.scheduledArrival = scheduledArrival;
        this.actualDeparture = actualDeparture;
        this.actualArrival = actualArrival;
        this.flightStatus = flightStatus;
        this.active = active;
        this.delayed = delayed;
        this.delayMinutes = delayMinutes;
        this.scheduledDurationMinutes = scheduledDurationMinutes;
        this.operational = operational;
    }

    public Long getId() {
        return id;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public Long getAirlineId() {
        return airlineId;
    }

    public Long getAircraftId() {
        return aircraftId;
    }

    public Long getDepartureAirportId() {
        return departureAirportId;
    }

    public Long getArrivalAirportId() {
        return arrivalAirportId;
    }

    public LocalDateTime getScheduledDeparture() {
        return scheduledDeparture;
    }

    public LocalDateTime getScheduledArrival() {
        return scheduledArrival;
    }

    public LocalDateTime getActualDeparture() {
        return actualDeparture;
    }

    public LocalDateTime getActualArrival() {
        return actualArrival;
    }

    public FlightStatus getFlightStatus() {
        return flightStatus;
    }

    public Boolean getActive() {
        return active;
    }

    public Boolean getDelayed() {
        return delayed;
    }

    public Long getDelayMinutes() {
        return delayMinutes;
    }

    public Long getScheduledDurationMinutes() {
        return scheduledDurationMinutes;
    }

    public Boolean getOperational() {
        return operational;
    }
}
