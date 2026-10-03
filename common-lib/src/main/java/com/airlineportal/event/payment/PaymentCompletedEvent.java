package com.airlineportal.event.payment;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentCompletedEvent {
    private Long paymentId;
    private Long bookingId;
    private Long userId;

    private String pnr;
    private BigDecimal amount;

//    private String razorpayOrderId;
    private String razorpayPaymentLinkId;
    private String razorpayPaymentId;

    private LocalDateTime occurredAt;


}
