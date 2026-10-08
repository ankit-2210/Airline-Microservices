package com.bookingservice.event;


import com.airlineportal.event.payment.PaymentRefundedEvent;
import com.airlineportal.utils.Booking.PaymentStatus;
import com.bookingservice.model.Booking;
import com.bookingservice.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class PaymentRefundedConsumer {
    private final BookingRepository bookingRepository;

    @KafkaListener(topics = "payment.refunded.v1", groupId = "booking-service")
    @Transactional
    public void handlePaymentRefunded(PaymentRefundedEvent event){
        Booking booking = bookingRepository.findById(event.getBookingId())
                        .orElseThrow(() -> new IllegalArgumentException("Booking not found with ID: " + event.getBookingId()));

        if(booking.getPaymentStatus() == PaymentStatus.REFUNDED)
            return;

        booking.setPaymentStatus(PaymentStatus.REFUNDED);
        bookingRepository.save(booking);

    }


}
