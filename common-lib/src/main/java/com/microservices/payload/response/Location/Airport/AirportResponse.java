package com.microservices.payload.response.Location.Airport;

import com.microservices.embeddable.Address;
import com.microservices.embeddable.GeoCode;
import com.microservices.payload.response.Location.City.CityResponse;
import lombok.*;

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
