package com.airlineportal.event.payment;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRefundedEvent {
    private Long paymentId;
    private Long bookingId;
    private Long userId;

    private String pnr;

    private BigDecimal refundAmount;

    private String gatewayRefundId;



}
