package com.paymentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentReportData {
    private Long id;
    private Long bookingId;
    private Long userId;

    private String pnr;
    private BigDecimal amount;

    private String paymentStatus;
    private String paymentMethod;

    private String transactionId;
    private String razorpayPaymentLinkId;
    private String razorpayPaymentId;

    private LocalDateTime paidAt;
    private LocalDateTime createdAt;


}
