package com.airlineportal.event.booking;


import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingCancelledEvent {
    private Long bookingId;

    private String pnr;

    private Long userId;
    private Long flightId;
    private Long flightInstanceId;

    private String cancellationReason;

    private LocalDateTime occurredAt;


}
