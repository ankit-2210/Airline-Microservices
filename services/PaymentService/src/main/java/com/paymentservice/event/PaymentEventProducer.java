package com.paymentservice.event;


import com.airlineportal.event.payment.PaymentRefundedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentEventProducer {
    private static final String PAYMENT_REFUNDED_TOPIC =
            "payment.refunded.v1";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishPaymentRefunded(PaymentRefundedEvent event){
        kafkaTemplate.send(PAYMENT_REFUNDED_TOPIC, String.valueOf(event.getBookingId()), event);
    }


}
