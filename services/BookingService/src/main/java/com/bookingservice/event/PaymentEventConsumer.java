package com.bookingservice.event;


import com.airlineportal.event.payment.PaymentCompletedEvent;
import com.airlineportal.event.payment.PaymentFailedEvent;
import com.airlineportal.utils.Booking.BookingStatus;
import com.airlineportal.utils.Booking.PaymentStatus;
import com.bookingservice.model.Booking;
import com.bookingservice.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class PaymentEventConsumer {
    private final BookingRepository bookingRepository;

    @KafkaListener(topics = "payment.completed.v1", groupId = "booking-service")
    @Transactional
    public void handlePaymentCompleted(PaymentCompletedEvent event){
        if (event == null || event.getBookingId() == null) {
            return;
        }

        Booking booking = bookingRepository.findById(event.getBookingId())
                .orElse(null);

        if (booking == null)
            return;

        /*
         * Idempotency:
         * If the payment event is delivered again,
         * don't process it again.
         */
        if (booking.getPaymentStatus() == PaymentStatus.SUCCESS)
            return;


        booking.setPaymentStatus(PaymentStatus.SUCCESS);
        booking.setBookingStatus(BookingStatus.CONFIRMED);
        booking.setConfirmedAt(LocalDateTime.now());

        bookingRepository.save(booking);
    }


    // PAYMENT FAILED
    @KafkaListener(topics = "payment.failed.v1", groupId = "booking-service")
    @Transactional
    public void handlePaymentFailed(PaymentFailedEvent event){
        if (event == null || event.getBookingId() == null)
            return;

        Booking booking = bookingRepository.findById(event.getBookingId())
                        .orElse(null);
        if (booking == null)
            return;

        /*
         * Idempotency:
         * If the payment is already failed,
         * don't process the event again.
         */
        if (booking.getPaymentStatus() == PaymentStatus.FAILED)
            return;

        booking.setPaymentStatus(PaymentStatus.FAILED);

        /*
         * Keep BookingStatus unchanged for now.
         *
         * You can later introduce a dedicated
         * payment-expired / booking-expired flow.
         */

        bookingRepository.save(booking);
    }

}
