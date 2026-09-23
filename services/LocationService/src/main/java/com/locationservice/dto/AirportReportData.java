package com.locationservice.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AirportReportData {
    private Long id;

    private String iataCode;

    private String name;

    private String cityName;
    private String cityCode;

    private String countryCode;
    private String countryName;

    private String timeZoneId;

    private Double latitude;
    private Double longitude;


}
