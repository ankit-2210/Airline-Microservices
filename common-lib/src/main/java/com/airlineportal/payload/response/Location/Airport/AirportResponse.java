package com.airlineportal.payload.response.Location.Airport;

import com.airlineportal.embeddable.Address;
import com.airlineportal.embeddable.GeoCode;
import com.airlineportal.payload.response.Location.City.CityResponse;
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

    private String timeZoneId;

}
