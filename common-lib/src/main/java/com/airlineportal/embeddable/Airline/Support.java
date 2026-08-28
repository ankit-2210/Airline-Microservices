package com.microservices.embeddable.Airline;

import jakarta.persistence.Embeddable;
import lombok.*;


@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Support {
    private String email;
    private String phone;
    private String hours;

}
