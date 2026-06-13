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
    @Size(max = 100)
    @Column(nullable = false)
    private String name;

    @NotBlank
    @Size(max = 20)
    @Column(nullable = false, unique = true, length = 20)
    private String cityCode;

    @NotBlank
    @Size(max = 10)
    @Column(nullable = false)
    private String countryCode;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false)
    private String countryName;

    @Size(max=10)
    private String regionCode;

    @Column(name = "time_zone_id", length = 50)
    private String timeZoneId;

}
