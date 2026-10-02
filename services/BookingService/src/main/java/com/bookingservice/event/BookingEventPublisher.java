package com.bookingservice.event;


import com.airlineportal.event.booking.BookingCancelledEvent;
import com.airlineportal.event.booking.BookingCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookingEventPublisher {
    private static final String BOOKING_CREATED_TOPIC = "booking.created.v1";
    private static final String BOOKING_CANCELLED_TOPIC = "booking.cancelled.v1";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishBookingCreated(BookingCreatedEvent event){
        kafkaTemplate.send(BOOKING_CREATED_TOPIC, String.valueOf(event.getBookingId()), event);
    }

    public void publishBookingCancelled(BookingCancelledEvent event){
        kafkaTemplate.send(BOOKING_CANCELLED_TOPIC, String.valueOf(event.getBookingId()), event);
    }


}
