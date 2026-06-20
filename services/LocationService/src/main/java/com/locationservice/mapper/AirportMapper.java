package com.locationservice.mapper;

import com.locationservice.model.Airport;
import com.microservices.payload.request.Location.Airport.AirportRequest;
import com.microservices.payload.response.Location.Airport.AirportResponse;

public class AirportMapper {
    public static Airport toEntity(AirportRequest airportRequest){
        if(airportRequest == null)
            return null;

        return Airport.builder()
                .iataCode(airportRequest.getIataCode().toUpperCase().trim())
                .name(airportRequest.getName().trim())

                .address(airportRequest.getAddress())
                .geoCode(airportRequest.getGeoCode())

                .timeZoneId(airportRequest.getTimeZoneId())
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

    public static void updateEntity(Airport airport, AirportRequest airportRequest){
        if(airport == null || airportRequest == null)
            return;

        if(airportRequest.getIataCode() != null){
            airport.setIataCode(airportRequest.getIataCode().toUpperCase().trim());
        }
        if(airportRequest.getName() != null){
            airport.setName(airportRequest.getName().trim());
        }
        if(airportRequest.getAddress() != null){
            airport.setAddress(airportRequest.getAddress());
        }
        if(airportRequest.getGeoCode() != null){
            airport.setGeoCode(airportRequest.getGeoCode());
        }
        if(airportRequest.getTimeZoneId() != null){
            airport.setTimeZoneId(airportRequest.getTimeZoneId());
        }

    }
}
