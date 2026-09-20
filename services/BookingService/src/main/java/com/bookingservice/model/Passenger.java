package com.bookingservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "passengers",
        indexes = {
                @Index(name = "idx_passenger_booking_id", columnList = "booking_id")
        }
)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Passenger {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @NotBlank
    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @NotBlank
    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @NotBlank
    @Column(name = "gender", nullable = false, length = 20)
    private String gender;

    @NotBlank
    @Column(name = "passport_number", length = 50)
    private String passportNumber;

    @Column(name = "seat_number", length = 10)
    private String seatNumber;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "booking_id", nullable = false)
    @ToString.Exclude
    private Booking booking;



}





















