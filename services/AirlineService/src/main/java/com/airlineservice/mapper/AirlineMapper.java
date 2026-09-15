package com.airlineservice.mapper;

import com.airlineportal.embeddable.Airline.Support;
import com.airlineportal.payload.request.Airlines.Airline.AirlineRequest;
import com.airlineportal.payload.response.Airlines.Airline.AirlineDropdownItem;
import com.airlineportal.payload.response.Airlines.Airline.AirlineResponse;
import com.airlineportal.utils.Airline.AirlineStatus;
import com.airlineservice.model.Airline;


public class AirlineMapper {
    private AirlineMapper() {
        // Utility class
    }

    // Request -> Entity
    public static Airline toEntity(AirlineRequest airlineRequest, Long ownerId){
        if(airlineRequest == null)
            return null;

        return Airline.builder()
                .iataCode(normalizeCode(airlineRequest.getIataCode()))
                .icaoCode(normalizeCode(airlineRequest.getIcaoCode()))

                .ownerId(ownerId)

                .name(airlineRequest.getName())
                .alias(airlineRequest.getAlias())
                .country(airlineRequest.getCountry())

                .logoUrl(airlineRequest.getLogoUrl())
                .website(airlineRequest.getWebsite())

                .airlineStatus(airlineRequest.getAirlineStatus() != null ? airlineRequest.getAirlineStatus() : AirlineStatus.ACTIVE)
                .alliance(normalize(airlineRequest.getAlliance()))

                .headquartersCityId(airlineRequest.getHeadquartersCityId())
                .support(buildSupport(airlineRequest))
                .build();
    }


    // Update Entity
    public static void updateEntity(Airline airline, AirlineRequest airlineRequest){
        if(airline == null || airlineRequest == null)
            return;

        airline.setIataCode(normalizeCode(airlineRequest.getIataCode()));
        airline.setIcaoCode(normalizeCode(airlineRequest.getIcaoCode()));
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

        airline.setSupport(buildSupport(airlineRequest));
    }

    // Entity -> Response
    public static AirlineResponse toResponse(Airline airline){
        if(airline == null)
            return null;

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

                .headquartersCityId(airline.getHeadquartersCityId())

                .updatedById(airline.getUpdatedById())
                .createdAt(airline.getCreatedAt())
                .updatedAt(airline.getUpdatedAt())
                .build();
    }

    // Entity -> Dropdown
    public static AirlineDropdownItem toDropdown(Airline airline) {
        if(airline == null)
            return null;

        return AirlineDropdownItem.builder()
                .id(airline.getId())

                .name(airline.getName())
                .iataCode(airline.getIataCode())
                .icaoCode(airline.getIcaoCode())
                .logoUrl(airline.getLogoUrl())
                .country(airline.getCountry())
                .build();
    }

    // Support Mapper
    private static Support buildSupport(AirlineRequest request){
        if(request == null)
            return null;

        String email = normalize(request.getSupportEmail());
        String phone = normalize(request.getSupportPhone());
        String hours = normalize(request.getSupportHours());

        if(email == null && phone == null && hours == null)
            return null;

        return Support.builder()
                .email(email)
                .phone(phone)
                .hours(hours)
                .build();
    }


    // Normalization
    private static String normalizeCode(String value){
        if(value == null)
            return null;

        return value.trim().toUpperCase();
    }

    private static String normalize(String value){
        if(value == null)
            return null;

        String normalized = value.trim();
        return normalized.isBlank() ? null : normalized;
    }

}
