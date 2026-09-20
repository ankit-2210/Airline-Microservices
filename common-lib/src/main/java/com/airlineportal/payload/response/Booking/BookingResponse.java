package com.airlineportal.payload.response.Booking;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponse {
    private Long id;

    private String pnr;

    private Long userId;
    private Long flightId;

    private BigDecimal totalAmount;

    private String bookingStatus;
    private String paymentStatus;

    private LocalDateTime bookedAt;
    private LocalDateTime confirmedAt;
    private LocalDateTime cancelledAt;

    private String cancellationReason;

    private List<PassengerResponse> passengers;

}


//{
//        "id": 1,
//        "pnr": "AB12CD34",
//        "userId": 25,
//        "flightId": 100,
//        "totalAmount": 12500.00,
//        "bookingStatus": "PENDING",
//        "paymentStatus": "PENDING",
//        "bookedAt": "2026-09-16T13:30:00",
//        "confirmedAt": null,
//        "cancelledAt": null,
//        "cancellationReason": null,
//        "passengers": [
//        {
//        "id": 1,
//        "firstName": "John",
//        "lastName": "Doe",
//        "gender": "MALE",
//        "passportNumber": "P1234567",
//        "seatNumber": "12A"
//        }
//        ]
//}
