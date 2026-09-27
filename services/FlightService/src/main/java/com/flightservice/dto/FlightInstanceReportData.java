package com.flightservice.dto;

import com.airlineportal.utils.Flight.FlightStatus;

import java.time.LocalDateTime;

public class FlightInstanceReportData {
    private Long id;
    private Long flightId;
    private String flightNumber;

    private LocalDateTime departureDateTime;
    private LocalDateTime arrivalDateTime;

    private Integer totalSeats;
    private Integer availableSeats;
    private Integer bookedSeats;

    private FlightStatus flightStatus;
    private Boolean active;

    private String formattedDuration;
    private Boolean soldOut;
    private Boolean bookingOpen;

    public FlightInstanceReportData(
            Long id,
            Long flightId,
            String flightNumber,
            LocalDateTime departureDateTime,
            LocalDateTime arrivalDateTime,
            Integer totalSeats,
            Integer availableSeats,
            Integer bookedSeats,
            FlightStatus flightStatus,
            Boolean active,
            String formattedDuration,
            Boolean soldOut,
            Boolean bookingOpen){

        this.id = id;
        this.flightId = flightId;
        this.flightNumber = flightNumber;
        this.departureDateTime = departureDateTime;
        this.arrivalDateTime = arrivalDateTime;
        this.totalSeats = totalSeats;
        this.availableSeats = availableSeats;
        this.bookedSeats = bookedSeats;
        this.flightStatus = flightStatus;
        this.active = active;
        this.formattedDuration = formattedDuration;
        this.soldOut = soldOut;
        this.bookingOpen = bookingOpen;
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

    public LocalDateTime getDepartureDateTime() {
        return departureDateTime;
    }

    public LocalDateTime getArrivalDateTime() {
        return arrivalDateTime;
    }

    public Integer getTotalSeats() {
        return totalSeats;
    }

    public Integer getAvailableSeats() {
        return availableSeats;
    }

    public Integer getBookedSeats() {
        return bookedSeats;
    }

    public FlightStatus getFlightStatus() {
        return flightStatus;
    }

    public Boolean getActive() {
        return active;
    }

    public String getFormattedDuration() {
        return formattedDuration;
    }

    public Boolean getSoldOut() {
        return soldOut;
    }

    public Boolean getBookingOpen() {
        return bookingOpen;
    }

}
