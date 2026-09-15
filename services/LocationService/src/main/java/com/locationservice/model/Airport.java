package com.locationservice.model;

import com.airlineportal.embeddable.Address;
import com.airlineportal.embeddable.GeoCode;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(
        name = "airports",
        indexes = {
                @Index(name = "idx_airport_iata_code", columnList = "iata_code"),
                @Index(name = "idx_airport_city_id", columnList = "city_id")
        }
)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Airport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @NotBlank
    @Size(min = 3, max = 3)
    @Column(name = "iata_code", unique = true, nullable = false, length = 3)
    private String iataCode;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @Embedded
    @Valid
    private Address address;

    @Embedded
    @Valid
    private GeoCode geoCode;

    @Column(name="time_zone_id", length = 50)
    private String timeZoneId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "city_id", nullable = false)
    @JsonIgnore
    @ToString.Exclude
    private City city;

    @JsonIgnore
    @Transient
    public String getDetailedName() {
        if (city != null && city.getCountryCode() != null) {
            return name.toUpperCase() + "/" + city.getCountryCode().toUpperCase();
        }
        return name.toUpperCase();
    }

}
