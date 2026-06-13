package com.microservices.payload.response.Airline;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AirlineDropdownItem {
    private Long id;
    private String name;

    private String iataCode;
    private String icaoCode;

    private String logoUrl;
    private String country;


}
