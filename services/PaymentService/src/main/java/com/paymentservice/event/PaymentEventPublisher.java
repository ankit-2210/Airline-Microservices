package com.paymentservice.event;


import com.airlineportal.event.Payment.PaymentCompletedEvent;
import com.airlineportal.event.Payment.PaymentFailedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentEventPublisher {
    private static final String PAYMENT_COMPLETED = "payment.completed.v1";
    private static final String PAYMENT_FAILED = "payment.failed.v1";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishPaymentCompleted(PaymentCompletedEvent event){
        kafkaTemplate.send(PAYMENT_COMPLETED, String.valueOf(event.getPaymentId()), event);
    }

    public void publishPaymentFailed(PaymentFailedEvent event){
        kafkaTemplate.send(PAYMENT_FAILED, String.valueOf(event.getPaymentId()), event);
    }


}
