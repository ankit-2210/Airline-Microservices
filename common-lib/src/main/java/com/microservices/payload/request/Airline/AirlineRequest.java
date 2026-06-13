package com.microservices.payload.request.Airline;

import com.microservices.utils.Airport.AirlineStatus;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AirlineRequest {

    @NotBlank(message = "IATA code is mandatory")
    @Size(min = 2, max = 2)
    private String iataCode;

    @NotBlank(message = "ICAO code is mandatory")
    @Size(min = 3, max = 3)
    private String icaoCode;

    @NotBlank(message = "airline name is mandatory")
    private String name;

    private String alias;

    @NotBlank(message = "Country is mandatory")
    private String country;

    private String logoUrl;

    private String website;

    private AirlineStatus airlineStatus;

    private String alliance;

    private Long headquartersCityId;

    private String supportEmail;
    private String supportPhone;
    private String supportHours;


}
