package com.paymentservice.event;

import com.airlineportal.event.Booking.BookingCreatedEvent;
import com.airlineportal.utils.Booking.PaymentStatus;
import com.paymentservice.model.Payment;
import com.paymentservice.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.math.*;

@Component
@RequiredArgsConstructor
public class BookingEventConsumer {
    private final PaymentRepository paymentRepository;

    @KafkaListener(topics = "booking.created.v1", groupId = "payment-service")
    @Transactional
    public void handleBookingCreated(BookingCreatedEvent event){
        if(event == null)
            return;

        // Prevent duplicate payment records
        if(paymentRepository.existsByBookingId(event.getBookingId()))
            return;

        if(event.getTotalAmount() == null || event.getTotalAmount().compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("Invalid booking amount: " + event.getTotalAmount());
        }

        Payment payment = Payment.builder()
                .bookingId(event.getBookingId())
                .userId(event.getUserId())

                .pnr(event.getPnr())
                .amount(event.getTotalAmount())

                .paymentStatus(PaymentStatus.PENDING)
                .createdAt(event.getOccurredAt())
                .build();

        paymentRepository.save(payment);
    }

}
