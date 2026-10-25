package com.notificationservice.event;


import com.airlineportal.event.booking.BookingCreatedEvent;
import com.notificationservice.service.NotificationIngestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookingEventConsumer {
    private final NotificationIngestionService ingestionService;

    @KafkaListener(topics = "booking.created.v1", groupId = "notification-service")
    public void onBookingCreated(BookingCreatedEvent event) {
//        if (event == null || event.getBookingId() == null || event.getUserId() == null) {
//            throw new IllegalArgumentException("Invalid booking-created event");
//        }
//
//        String key = "booking-created:" + event.getBookingId();
//        String body = """
//            Hello,
//
//            Your flight booking has been received.
//
//            Booking ID: %s
//            PNR: %s
//            Amount: %s
//            Passenger count: %s
//
//            We will notify you about your payment and booking status.
//
//            Regards,
//            Airline Portal
//            """.formatted(
//                event.getBookingId(),
//                safe(event.getPnr()),
//                event.getTotalAmount() == null
//                        ? "N/A" : event.getTotalAmount().toPlainString(),
//                event.getPassengerCount() == null
//                        ? "N/A" : event.getPassengerCount()
//        );
//
//        ingestionService.ingest(
//                key,
//                "BOOKING_CREATED",
//                event.getUserId(),
//                "Booking received - " + safe(event.getPnr()),
//                body
//        );



    }

}
