package com.locationservice.mapper;

import com.locationservice.model.Airport;
import com.microservices.payload.request.Location.Airport.AirportRequest;
import com.microservices.payload.response.Airport.AirportResponse;

public class AirportMapper {
    public static Airport toEntity(AirportRequest airportRequest){
        if(airportRequest == null)
            return null;

        return Airport.builder()
                .iataCode(airportRequest.getIataCode())
                .name(airportRequest.getName())
                .address(airportRequest.getAddress())
                .geoCode(airportRequest.getGeoCode())
                .timeZoneId(airportRequest.getTimeZone())
                .build();
    }

    public static AirportResponse toResponse(Airport airport){
        if(airport == null)
            return null;

        return AirportResponse.builder()
                .id(airport.getId())
                .iataCode(airport.getIataCode())
                .name(airport.getName())
                .address(airport.getAddress())
                .geoCode(airport.getGeoCode())
                .city(CityMapper.toResponse(airport.getCity()))
                .timeZone(airport.getTimeZoneId())
                .detailedName(airport.getDetailedName())
                .build();
    }

    public static void updateEntity(Airport airport, AirportRequest airportRequest){
        if(airport == null || airportRequest == null)
            return;

        if(airportRequest.getIataCode() != null){
            airport.setIataCode(airportRequest.getIataCode());
        }
        if(airportRequest.getName() != null){
            airport.setName(airportRequest.getName());
        }
        if(airportRequest.getAddress() != null){
            airport.setAddress(airportRequest.getAddress());
        }
        if(airportRequest.getGeoCode() != null){
            airport.setGeoCode(airportRequest.getGeoCode());
        }

    }
}
