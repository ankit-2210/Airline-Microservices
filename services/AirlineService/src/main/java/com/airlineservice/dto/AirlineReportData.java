package com.airlineservice.dto;

import com.airlineportal.utils.Airline.AirlineStatus;

public class AirlineReportData {
    private Long id;
    private String iataCode;
    private String icaoCode;
    private String name;
    private String country;
    private String alliance;
    private AirlineStatus airlineStatus;
    private Integer aircraftCount;

    public AirlineReportData(
            Long id,
            String iataCode,
            String icaoCode,
            String name,
            String country,
            String alliance,
            AirlineStatus airlineStatus,
            Integer aircraftCount
    ) {
        this.id = id;
        this.iataCode = iataCode;
        this.icaoCode = icaoCode;
        this.name = name;
        this.country = country;
        this.alliance = alliance;
        this.airlineStatus = airlineStatus;
        this.aircraftCount = aircraftCount;
    }

    public Long getId() {
        return id;
    }

    public String getIataCode() {
        return iataCode;
    }

    public String getIcaoCode() {
        return icaoCode;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public String getAlliance() {
        return alliance;
    }

    public AirlineStatus getAirlineStatus() {
        return airlineStatus;
    }

    public Integer getAircraftCount() {
        return aircraftCount;
    }

}
