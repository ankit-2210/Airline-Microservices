package com.locationservice.mapper;

import com.locationservice.model.City;
import com.microservices.payload.request.City.CityRequest;
import com.microservices.payload.response.City.CityResponse;

public class CityMapper {
    public static City toEntity(CityRequest cityRequest){
        if(cityRequest == null)
            return null;

        return City.builder()
                .name(cityRequest.getName())
                .cityCode(cityRequest.getCityCode())
                .countryCode(cityRequest.getCountryCode())
                .countryName(cityRequest.getCountryName())
                .regionCode(cityRequest.getRegionCode())
                .timeZoneId(cityRequest.getTimeZoneOffset())
                .build();
    }

    public static CityResponse toResponse(City city){
        if(city == null)
            return null;

        return CityResponse.builder()
                .id(city.getId())
                .name(city.getName())
                .cityCode(city.getCityCode())
                .countryCode(city.getCityCode())
                .countryName(city.getCountryName())
                .regionCode(city.getRegionCode())
                .build();
    }
    public static City updateEntity(City city, CityRequest cityRequest){
        if(cityRequest.getName() != null){
            city.setName(cityRequest.getName().trim());
        }
        if(cityRequest.getCityCode() != null){
            city.setCityCode(cityRequest.getCityCode().toUpperCase().trim());
        }
        if(cityRequest.getCountryCode() != null){
            city.setCityCode(cityRequest.getCountryCode().toUpperCase().trim());
        }
        if(cityRequest.getCountryName() != null){
            city.setCountryName(cityRequest.getCountryName().trim());
        }
        if(cityRequest.getRegionCode() != null){
            city.setRegionCode(cityRequest.getRegionCode().toUpperCase().trim());
        }
        return city;
    }
}
