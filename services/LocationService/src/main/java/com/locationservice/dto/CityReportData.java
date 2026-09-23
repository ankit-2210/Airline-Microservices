package com.locationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CityReportData {
    private Long id;

    private String name;

    private String cityCode;

    private String countryCode;
    private String countryName;

    private String regionCode;

    private String timeZoneId;
    private Integer airportCount;


}
