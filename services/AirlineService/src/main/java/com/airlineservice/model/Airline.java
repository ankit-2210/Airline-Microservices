package com.airlineservice.model;

import com.airlineportal.utils.Airline.AirlineStatus;
import com.microservices.embeddable.Airline.Support;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(
        name = "airlines",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_airline_iata_code", columnNames = "iata_code"),
                @UniqueConstraint(name = "uk_airline_icao_code", columnNames = "icao_code"),
                @UniqueConstraint(name = "uk_airline_owner", columnNames = "owner_id")
        },
        indexes = {
                @Index(name = "idx_airline_owner", columnList = "owner_id"),
                @Index(name = "idx_airline_status", columnList = "airline_status"),
                @Index(name = "idx_airline_country", columnList = "country"),
                @Index(name = "idx_airline_name", columnList = "name")
        }
)
@EntityListeners(AuditingEntityListener.class)
public class Airline {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 2, max = 2)
    @Column(name = "iata_code", unique = true, nullable = false, length = 2)
    private String iataCode;

    @NotBlank
    @Size(min = 3, max = 3)
    @Column(name = "icao_code", unique = true, nullable = false, length = 3)
    private String icaoCode;

    @Column(name = "owner_id", nullable = false)
    private Long ownerId;

    @NotBlank
    @Column(nullable = false, length = 150)
    private String name;

    @Column(length = 150)
    private String alias;

    @NotBlank
    @Column(nullable = false, length = 100)
    private String country;

    @Column(name = "logo_url")
    private String logoUrl;

    private String website;

    @Enumerated(EnumType.STRING)
    @Column(name = "airline_status", nullable = false, length = 30)
    private AirlineStatus airlineStatus = AirlineStatus.ACTIVE;

    @Column(length = 100)
    private String alliance;

    @Embedded
    private Support support;

    @Column(name = "headquarters_city_id")
    private Long headquartersCityId;

    private Long updatedById;

    @OneToMany(mappedBy = "airline", fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    private List<Aircraft> aircraft = new ArrayList<>();

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private Instant updatedAt;

    @Version
    @Column(nullable = false)
    private Long version;


    public void addAircraft(Aircraft aircraft){
        if(aircraft == null)
            return;

        if(!this.aircraft.contains(aircraft)){
            this.aircraft.add(aircraft);
        }

        aircraft.setAirline(this);
    }

    public void removeAircraft(Aircraft aircraft){
        if(aircraft == null)
            return;

        this.aircraft.remove(aircraft);
        if(aircraft.getAirline() == this){
            aircraft.setAirline(null);
        }
    }


}
