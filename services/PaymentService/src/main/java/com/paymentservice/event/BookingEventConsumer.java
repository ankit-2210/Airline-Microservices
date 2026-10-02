package com.paymentservice.event;

import com.airlineportal.event.Booking.BookingCreatedEvent;
import com.paymentservice.model.Payment;
import com.paymentservice.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class BookingEventConsumer {
    private final PaymentRepository paymentRepository;

    @KafkaListener(topics = "booking.created.v1", groupId = "payment-service")
    @Transactional
    public void handleBookingCreated(BookingCreatedEvent event) {
        if (event == null) {
            return;
        }
        

    }


}
