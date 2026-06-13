package com.airlineservice.model;

import com.microservices.embeddable.Airline.Support;
import com.microservices.utils.Airport.AirlineStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@Table(
        name = "airlines",
        indexes = {
                @Index(name = "idx_airline_owner", columnList = "ownerId"),
                @Index(name = "idx_airline_status", columnList = "airlineStatus")
        }
)
public class Airline {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true, nullable = false, length = 2)
    private String iataCode;

    @Column(unique = true, nullable = false, length = 3)
    private String icaoCode;

    @Column(nullable = false)
    private Long ownerId;

    @NotBlank
    @Column(nullable = false)
    private String name;

    private String alias;

    @Column(nullable = false)
    private String country;

    private String logoUrl;
    private String website;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AirlineStatus airlineStatus = AirlineStatus.ACTIVE;

    private String alliance;

    @Embedded
    private Support support;

    private Long headquartersCityId;

    private Long updatedById;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private Instant updatedAt;


}
