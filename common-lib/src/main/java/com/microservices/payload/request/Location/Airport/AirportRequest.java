package com.microservices.payload.request.Location.Airport;

import com.microservices.embeddable.Address;
import com.microservices.embeddable.GeoCode;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.ZoneId;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AirportRequest {

    @NotBlank(message = "IATA code is mandatory")
    @Size(min=3, max=3, message = "IATA code must be exactly 3 characters")
    private String iataCode;

    @NotBlank(message = "Airport name is mandatory")
    private String name;

    private ZoneId timeZone;

    @Valid
    private Address address;

    @NotNull(message = "City Id is mandatory")
    private Long cityId;

    @Valid
    private GeoCode geoCode;
}
