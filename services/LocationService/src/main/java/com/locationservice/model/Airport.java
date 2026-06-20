package com.locationservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.microservices.embeddable.Address;
import com.microservices.embeddable.GeoCode;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.ZoneId;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(
        name = "airports",
        indexes = {
                @Index(name = "idx_iata_code", columnList = "iataCode"),
                @Index(name = "idx_city_id", columnList = "city_id")
        }
)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Airport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @NotBlank
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

    @ManyToOne(fetch = FetchType.LAZY)
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
