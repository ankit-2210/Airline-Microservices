package com.locationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

//@Getter
//@AllArgsConstructor
public class CityReportData {
    private Long id;
    private String name;
    private String cityCode;
    private String countryCode;
    private String countryName;
    private String regionCode;
    private String timeZoneId;
    private Integer airportCount;

    public CityReportData(
            Long id,
            String name,
            String cityCode,
            String countryCode,
            String countryName,
            String regionCode,
            String timeZoneId,
            Integer airportCount) {

        this.id = id;
        this.name = name;
        this.cityCode = cityCode;
        this.countryCode = countryCode;
        this.countryName = countryName;
        this.regionCode = regionCode;
        this.timeZoneId = timeZoneId;
        this.airportCount = airportCount;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCityCode() {
        return cityCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public String getTimeZoneId() {
        return timeZoneId;
    }

    public Integer getAirportCount() {
        return airportCount;
    }

}
