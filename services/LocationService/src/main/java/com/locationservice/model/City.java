package com.locationservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(
        name = "airports",
        indexes = {
                @Index(name = "idx_city_code", columnList = "city_ode"),
                @Index(name = "idx_country_id", columnList = "country_id")
        }
)
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false)
    private String name;

    @NotBlank
    @Size(max = 20)
    @Column(name = "city_code", nullable = false, unique = true)
    private String cityCode;

    @NotBlank
    @Size(max = 10)
    @Column(name = "country_code", nullable = false)
    private String countryCode;

    @NotBlank
    @Size(max = 100)
    @Column(name = "country_name", nullable = false)
    private String countryName;

    @Size(max=10)
    private String regionCode;

    @Column(name = "time_zone_id", length = 50)
    private String timeZoneId;

    @OneToMany(mappedBy = "city", fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude
    @Builder.Default
    private List<Airport> airports = new ArrayList<>();

}
