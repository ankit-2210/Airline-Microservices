package com.microservices.payload.request.Airlines.Airline;

import com.microservices.utils.Airline.AirlineStatus;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AirlineRequest {

    @NotBlank(message = "IATA code is mandatory")
    @Size(min = 2, max = 2, message = "IATA code must be exactly 2 characters")
    @Pattern(regexp = "^[A-Za-z]{2}$", message = "IATA code must contain only letters")
    private String iataCode;

    @NotBlank(message = "ICAO code is mandatory")
    @Size(min = 3, max = 3, message = "ICAO code must be exactly 3 characters")
    @Pattern(regexp = "^[A-Za-z]{3}$", message = "ICAO code must contain only letters")
    private String icaoCode;

    @NotBlank(message = "airline name is mandatory")
    @Size(max = 100)
    private String name;

    @Size(max = 100)
    private String alias;

    @NotBlank(message = "Country is mandatory")
    private String country;

    private String logoUrl;
    private String website;

    private AirlineStatus airlineStatus;

    private String alliance;

    private Long headquartersCityId;

    @Email(message = "Invalid support email")
    private String supportEmail;
    private String supportPhone;
    private String supportHours;


}
