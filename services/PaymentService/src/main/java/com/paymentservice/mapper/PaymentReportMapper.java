package com.paymentservice.mapper;

import com.paymentservice.dto.PaymentReportData;
import com.paymentservice.model.Payment;

public class PaymentReportMapper {
    private PaymentReportMapper(){

    }

    public static PaymentReportData toReportData(Payment payment) {

        return PaymentReportData.builder()
                .id(payment.getId())
                .bookingId(payment.getBookingId())
                .userId(payment.getUserId())

                .pnr(payment.getPnr())
                .amount(payment.getAmount())
                .paymentStatus(payment.getPaymentStatus() != null ? payment.getPaymentStatus().name() : null)
                .paymentMethod(payment.getPaymentMethod())

                .transactionId(payment.getTransactionId())
                .razorpayPaymentLinkId(payment.getRazorpayPaymentLinkId())
                .razorpayPaymentId(payment.getRazorpayPaymentId())

                .paidAt(payment.getPaidAt())
                .createdAt(payment.getCreatedAt())
                .build();
    }


}
