package com.locationservice.mapper;

import com.airlineportal.payload.request.Location.Airport.AirportRequest;
import com.airlineportal.payload.response.Location.Airport.AirportResponse;
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
}
