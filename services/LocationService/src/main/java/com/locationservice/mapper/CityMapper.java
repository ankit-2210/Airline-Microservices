package com.locationservice.mapper;

import com.airlineportal.payload.request.Location.City.CityRequest;
import com.airlineportal.payload.response.Location.City.CityResponse;
import com.locationservice.model.City;


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
                .timeZoneId(cityRequest.getTimeZoneId())
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
                .timeZoneId(city.getTimeZoneId())
                .build();
    }
    public static void updateEntity(City city, CityRequest request){
        if(city == null || request == null)
            return;

        city.setName(request.getName());
        city.setCityCode(request.getCityCode());
        city.setCountryCode(request.getCountryCode());
        city.setCountryName(request.getCountryName());
        city.setRegionCode(request.getRegionCode());
        city.setTimeZoneId(request.getTimeZoneId());


    }
}
