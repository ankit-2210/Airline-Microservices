package com.microservices.payload.response.Airlines.Airline;

import com.microservices.embeddable.Airline.Support;
import com.microservices.payload.dto.UserDto;
import com.microservices.payload.response.Location.City.CityResponse;
import com.microservices.utils.Airline.AirlineStatus;
import lombok.*;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AirlineResponse {

    private Long id;

    private String iataCode;
    private String icaoCode;

    private String name;
    private String alias;

    private String country;

    private String logoUrl;
    private String website;

    private AirlineStatus airlineStatus;
    private String alliance;

    private Long ownerId;
    private UserDto owner;
    private Long updatedById;

    private CityResponse headquartersCity;
    private Support support;

    private Integer aircraftCount;

    private Instant createdAt;
    private Instant updatedAt;

}
