package com.airlineservice.mapper;

import com.airlineservice.model.Airline;
import com.microservices.embeddable.Airline.Support;
import com.microservices.payload.request.Airlines.Airline.AirlineRequest;
import com.microservices.payload.response.Airlines.Airline.AirlineDropdownItem;
import com.microservices.payload.response.Airlines.Airline.AirlineResponse;

public class AirlineMapper {
    public static Airline toEntity(AirlineRequest airlineRequest, Long ownerId){
        if(airlineRequest == null)
            return null;

        return Airline.builder()
                .iataCode(airlineRequest.getIataCode().toUpperCase())
                .icaoCode(airlineRequest.getIcaoCode().toUpperCase())
                .name(airlineRequest.getName())
                .alias(airlineRequest.getAlias())
                .country(airlineRequest.getCountry())
                .logoUrl(airlineRequest.getLogoUrl())
                .website(airlineRequest.getWebsite())
                .airlineStatus(airlineRequest.getAirlineStatus())
                .alliance(airlineRequest.getAlliance())
                .ownerId(ownerId)
                .headquartersCityId(airlineRequest.getHeadquartersCityId())
                .support(
                        Support.builder()
                                .email(airlineRequest.getSupportEmail())
                                .phone(airlineRequest.getSupportPhone())
                                .hours(airlineRequest.getSupportHours())
                                .build()
                )
                .build();

    }

    public static void updateEntity(Airline airline, AirlineRequest airlineRequest){
        airline.setIataCode(airlineRequest.getIataCode().toUpperCase());
        airline.setIcaoCode(airlineRequest.getIcaoCode().toUpperCase());
        airline.setName(airlineRequest.getName());
        airline.setAlias(airlineRequest.getAlias());
        airline.setCountry(airlineRequest.getCountry());
        airline.setLogoUrl(airlineRequest.getLogoUrl());
        airline.setWebsite(airlineRequest.getWebsite());
        airline.setAlliance(airlineRequest.getAlliance());
        airline.setHeadquartersCityId(airlineRequest.getHeadquartersCityId());

        if (airlineRequest.getAirlineStatus() != null) {
            airline.setAirlineStatus(airlineRequest.getAirlineStatus());
        }

        airline.setSupport(
                Support.builder()
                        .email(airlineRequest.getSupportEmail())
                        .phone(airlineRequest.getSupportPhone())
                        .hours(airlineRequest.getSupportHours())
                        .build()
        );
    }


    public static AirlineResponse toResponse(Airline airline) {

        return AirlineResponse.builder()
                .id(airline.getId())
                .iataCode(airline.getIataCode())
                .icaoCode(airline.getIcaoCode())
                .name(airline.getName())
                .alias(airline.getAlias())
                .country(airline.getCountry())
                .logoUrl(airline.getLogoUrl())
                .website(airline.getWebsite())
                .airlineStatus(airline.getAirlineStatus())
                .alliance(airline.getAlliance())
                .ownerId(airline.getOwnerId())
                .updatedById(airline.getUpdatedById())
                .createdAt(airline.getCreatedAt())
                .updatedAt(airline.getUpdatedAt())
                .support(airline.getSupport())
                .build();
    }

    public static AirlineDropdownItem toDropdown(Airline airline) {

        return AirlineDropdownItem.builder()
                .id(airline.getId())
                .name(airline.getName())
                .iataCode(airline.getIataCode())
                .icaoCode(airline.getIcaoCode())
                .logoUrl(airline.getLogoUrl())
                .country(airline.getCountry())
                .build();
    }




}
