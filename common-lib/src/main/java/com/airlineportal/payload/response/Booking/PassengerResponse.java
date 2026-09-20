package com.airlineportal.payload.response.Booking;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PassengerResponse {
    private Long id;

    private String firstName;
    private String lastName;

    private String gender;

    private String passportNumber;
    private String seatNumber;


}
