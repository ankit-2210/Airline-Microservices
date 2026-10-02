package com.paymentservice.mapper;

import com.airlineportal.event.Payment.PaymentCompletedEvent;
import com.airlineportal.event.Payment.PaymentFailedEvent;
import com.airlineportal.payload.request.Payment.PaymentRequest;
import com.airlineportal.payload.response.Payment.PaymentResponse;
import com.paymentservice.event.PaymentEventPublisher;
import com.paymentservice.model.Payment;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

public final class PaymentMapper {
    private PaymentMapper(){

    }

    public static Payment toEntity(PaymentRequest request){
        if(request == null)
            return null;

        return Payment.builder()
                .bookingId(request.getBookingId())
                .userId(request.getUserId())
                .amount(request.getAmount())
                .paymentMethod(request.getPaymentMethod())
                .build();
    }

    public static PaymentResponse toResponse(Payment payment){
        if(payment == null)
            return null;

        return PaymentResponse.builder()
                .id(payment.getId())
                .bookingId(payment.getBookingId())
                .userId(payment.getUserId())

                .pnr(payment.getPnr())
                .amount(payment.getAmount())

                .paymentStatus(payment.getPaymentStatus() == null ? null : payment.getPaymentStatus().name())
                .paymentMethod(payment.getPaymentMethod())

                .transactionId(payment.getTransactionId())
                .razorpayOrderId(payment.getRazorpayOrderId())
                .razorpayPaymentId(payment.getRazorpayPaymentId())

                .paidAt(payment.getPaidAt())
                .createdAt(payment.getCreatedAt())
                .updatedAt(payment.getUpdatedAt())
                .build();
    }





}
