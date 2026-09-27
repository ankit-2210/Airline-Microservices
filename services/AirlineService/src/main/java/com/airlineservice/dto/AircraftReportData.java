package com.airlineservice.dto;

import com.airlineportal.utils.Airline.AircraftStatus;

import java.time.LocalDate;

public class AircraftReportData {
    private Long id;
    private String code;
    private String model;
    private String manufacturer;
    private Integer seatingCapacity;
    private AircraftStatus aircraftStatus;
    private Boolean isAvailable;
    private Long currentAirportId;
    private LocalDate nextMaintenanceDate;
    private Integer totalSeats;
    private Boolean operational;

    public AircraftReportData(
            Long id,
            String code,
            String model,
            String manufacturer,
            Integer seatingCapacity,
            AircraftStatus aircraftStatus,
            Boolean isAvailable,
            Long currentAirportId,
            LocalDate nextMaintenanceDate,
            Integer totalSeats,
            Boolean operational
    ) {
        this.id = id;
        this.code = code;
        this.model = model;
        this.manufacturer = manufacturer;
        this.seatingCapacity = seatingCapacity;
        this.aircraftStatus = aircraftStatus;
        this.isAvailable = isAvailable;
        this.currentAirportId = currentAirportId;
        this.nextMaintenanceDate = nextMaintenanceDate;
        this.totalSeats = totalSeats;
        this.operational = operational;
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getModel() {
        return model;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public Integer getSeatingCapacity() {
        return seatingCapacity;
    }

    public AircraftStatus getAircraftStatus() {
        return aircraftStatus;
    }

    public Boolean getIsAvailable() {
        return isAvailable;
    }

    public Long getCurrentAirportId() {
        return currentAirportId;
    }

    public LocalDate getNextMaintenanceDate() {
        return nextMaintenanceDate;
    }

    public Integer getTotalSeats() {
        return totalSeats;
    }

    public Boolean getOperational() {
        return operational;
    }

}
