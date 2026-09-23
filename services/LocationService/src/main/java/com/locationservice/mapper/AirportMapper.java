package com.locationservice.mapper;

import com.airlineportal.payload.request.Location.Airport.AirportRequest;
import com.airlineportal.payload.response.Location.Airport.AirportResponse;
import com.locationservice.dto.AirportReportData;
import com.locationservice.model.Airport;
import com.locationservice.model.City;

public class AirportMapper {
    public static Airport toEntity(AirportRequest request, City city){
        if(request == null)
            return null;

        return Airport.builder()
                .iataCode(request.getIataCode())
                .name(request.getName())

                .address(request.getAddress())
                .geoCode(request.getGeoCode())

                .timeZoneId(request.getTimeZoneId())
                .city(city)
                .build();
    }

    public static AirportResponse toResponse(Airport airport){
        if(airport == null)
            return null;

        return AirportResponse.builder()
                .id(airport.getId())

                .iataCode(airport.getIataCode())

                .name(airport.getName())
                .detailedName(airport.getDetailedName())

                .address(airport.getAddress())
                .geoCode(airport.getGeoCode())
                .city(CityMapper.toResponse(airport.getCity()))

                .timeZoneId(airport.getTimeZoneId())
                .build();
    }

    public static void updateEntity(Airport airport, AirportRequest request, City city){
        if(airport == null || request == null)
            return;

        airport.setIataCode(request.getIataCode());
        airport.setName(request.getName());
        airport.setAddress(request.getAddress());
        airport.setGeoCode(request.getGeoCode());
        airport.setTimeZoneId(request.getTimeZoneId());
        airport.setCity(city);

    }


    // Airport Report Mapping
    public static AirportReportData toAirportReportData(Airport airport){
        Double latitude = null;
        Double longitude = null;

        if(airport.getGeoCode() != null) {
            latitude = airport.getGeoCode().getLatitude();
            longitude = airport.getGeoCode().getLongitude();
        }

        return new AirportReportData(
                airport.getId(),
                airport.getIataCode(),
                airport.getName(),

                airport.getCity() != null ? airport.getCity().getName() : null,
                airport.getCity() != null ? airport.getCity().getCityCode() : null,
                airport.getCity() != null ? airport.getCity().getCountryCode() : null,
                airport.getCity() != null ? airport.getCity().getCountryName() : null,

                airport.getTimeZoneId(),

                latitude,
                longitude
        );


    }







}
