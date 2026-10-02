package com.paymentservice.event;

import com.airlineportal.event.Booking.BookingCreatedEvent;
import com.airlineportal.utils.Booking.PaymentStatus;
import com.paymentservice.model.Payment;
import com.paymentservice.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class PaymentEventConsumer {
    private final PaymentRepository paymentRepository;

    @KafkaListener(topics = "booking.created.v1", groupId = "payment-service")
    @Transactional
    public void handleBookingCreated(BookingCreatedEvent event){
        if(event == null)
            return;

        if(paymentRepository.existsByBookingId(event.getBookingId()))
            return;

        Payment payment = Payment.builder()
                .bookingId(event.getBookingId())
                .userId(event.getUserId())
                .pnr(event.getPnr())
                .amount(event.getTotalAmount())
                .paymentStatus(PaymentStatus.PENDING)
                .build();

        paymentRepository.save(payment);
    }

}
