package com.airlineportal.payload.request.Location.Airport;

import com.airlineportal.embeddable.Address;
import com.airlineportal.embeddable.GeoCode;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AirportRequest {

    @NotBlank(message = "IATA code is mandatory")
    @Size(
            min = 3,
            max = 3,
            message = "IATA code must be exactly 3 characters")
    private String iataCode;

    @NotBlank(message = "Airport name is mandatory")
    @Size(
            max = 255,
            message = "Airport name must not exceed 255 characters"
    )
    private String name;

    @Valid
    private Address address;

    @Valid
    private GeoCode geoCode;

    @NotBlank(message = "Timezone is required")
    @Size(
            max = 50,
            message = "Timezone must not exceed 50 characters"
    )
    private String timeZoneId;

    @NotNull(message = "City Id is mandatory")
    private Long cityId;


}
