package com.microservices.payload.response.Airport;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.microservices.embeddable.Address;
import com.microservices.embeddable.GeoCode;
import com.microservices.payload.response.City.CityResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZoneId;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AirportResponse {
    private Long id;
    private String iataCode;
    private String name;
    private String detailedName;
    private Address address;
    private GeoCode geoCode;
    private CityResponse city;
    private ZoneId timeZone;

}
