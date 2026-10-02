package com.airlineportal.payload.response.Payment;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {
    private Long id;
    private Long bookingId;
    private Long userId;

    private String pnr;

    private BigDecimal amount;

    private String paymentStatus;
    private String paymentMethod;

    private String transactionId;
    private String razorpayOrderId;
    private String razorpayPaymentId;
    private String razorpayKeyId;

    private LocalDateTime paidAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
