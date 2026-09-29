package com.bookingservice.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PassengerReportData {
    private Long id;
    private Long bookingId;

    private String pnr;

    private String firstName;
    private String lastName;
    private String gender;

    private String passportNumber;
    private String seatNumber;


}
