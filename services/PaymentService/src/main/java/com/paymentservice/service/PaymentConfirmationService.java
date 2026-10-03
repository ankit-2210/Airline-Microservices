package com.paymentservice.service;

import com.airlineportal.event.payment.PaymentCompletedEvent;
import com.airlineportal.utils.Booking.PaymentStatus;
import com.paymentservice.event.PaymentEventPublisher;
import com.paymentservice.model.Payment;
import com.paymentservice.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentConfirmationService {
    private final PaymentRepository paymentRepository;
    private final PaymentEventPublisher paymentEventPublisher;

    @Transactional
    public Payment confirmPayment(String razorpayPaymentLinkId, String razorpayPaymentId) {
        Payment payment = paymentRepository.findByRazorpayPaymentLinkId(razorpayPaymentLinkId)
                        .orElseThrow(() -> new IllegalArgumentException("Payment not found for Payment Link: " + razorpayPaymentLinkId));

        // Idempotency
        if (payment.getPaymentStatus() == PaymentStatus.SUCCESS) {
            return payment;
        }

        // Basic validation
        if (razorpayPaymentId == null || razorpayPaymentId.isBlank()) {
            throw new IllegalArgumentException("Razorpay Payment ID is required");
        }

        if (payment.getAmount() == null || payment.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Invalid local payment amount");
        }

        // Update payment
        payment.setRazorpayPaymentId(razorpayPaymentId);
        payment.setTransactionId(razorpayPaymentId);
        payment.setPaymentStatus(PaymentStatus.SUCCESS);
        payment.setPaidAt(LocalDateTime.now());
        payment.setUpdatedAt(LocalDateTime.now());

        Payment saved = paymentRepository.save(payment);

        // Publish Kafka event
        PaymentCompletedEvent event = PaymentCompletedEvent.builder()
                        .paymentId(saved.getId())
                        .bookingId(saved.getBookingId())
                        .userId(saved.getUserId())

                        .pnr(saved.getPnr())
                        .amount(saved.getAmount())

                        .razorpayPaymentLinkId(saved.getRazorpayPaymentLinkId())
                        .razorpayPaymentId(saved.getRazorpayPaymentId())
                        .occurredAt(LocalDateTime.now())
                        .build();

        paymentEventPublisher.publishPaymentCompleted(event);

        System.out.println("Payment SUCCESS: " + saved.getId());
        return saved;
    }

}
