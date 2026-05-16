package com.locationservice.model;

import jakarta.persistence.*;
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
        name="cities",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = "cityCode")
        })
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @NotBlank
    @Column(nullable = false)
    private String cityCode;

    @NotBlank
    @Column(nullable = false)
    private String countryCode;

    @NotBlank
    @Column(nullable = false)
    private String countryName;

    @Size(max=10)
    private String regionCode;

    @Column(name = "time_zone_id", length = 50)
    private String timeZoneId;

}
