package com.airlineportal.event.Booking;

import lombok.*;

import java.math.*;
import java.time.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingCreatedEvent {
    private Long bookingId;

    private String pnr;

    private Long userId;
    private Long flightId;
    private Long flightInstanceId;

    private BigDecimal totalAmount;
    private Integer passengerCount;

    private LocalDateTime occurredAt;


}
