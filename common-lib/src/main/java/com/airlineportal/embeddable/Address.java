package com.airlineportal.embeddable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {
    @Column(name = "street", length = 255)
    private String street;

    @Column(name = "postal_code", length = 20)
    private String postalCode;
}
