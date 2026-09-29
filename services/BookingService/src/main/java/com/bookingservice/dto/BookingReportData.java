package com.bookingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class BookingReportData {
    private Long id;

    private String pnr;

    private Long userId;
    private Long flightId;
    private Long flightInstanceId;

    private BigDecimal totalAmount;

    private String bookingStatus;
    private String paymentStatus;

    private LocalDateTime bookedAt;
    private LocalDateTime confirmedAt;
    private LocalDateTime cancelledAt;

}
