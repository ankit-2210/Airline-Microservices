package com.paymentservice.service;


import com.airlineportal.event.payment.PaymentCompletedEvent;
import com.airlineportal.event.payment.PaymentFailedEvent;
import com.airlineportal.utils.Booking.PaymentStatus;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymentservice.event.PaymentEventPublisher;
import com.paymentservice.external.RazorpayService;
import com.paymentservice.model.Payment;
import com.paymentservice.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentWebhookService {
    private final RazorpayService razorpayService;
    private final PaymentConfirmationService paymentConfirmationService;
    private final ObjectMapper objectMapper;

    @Transactional
    public void handleWebhook(String payload, String signature){
        // 1. Verify webhook signature
        boolean valid = razorpayService.verifyWebhookSignature(payload, signature);
        if(!valid){
            throw new IllegalArgumentException("Invalid Razorpay webhook signature");
        }

        try {
            // 2. Parse payload
            JsonNode root = objectMapper.readTree(payload);

            String event = root.path("event").asText();
            System.out.println("Razorpay webhook event: " + event);

            // 3. Handle Payment Link paid
            if("payment_link.paid".equals(event)) {
                handlePaymentLinkPaid(root);
            }
            else{
                System.out.println("Ignoring webhook event: " + event);
            }
        }
        catch (Exception e){
            throw new IllegalStateException("Unable to process Razorpay webhook", e);
        }
    }


    private void handlePaymentLinkPaid(JsonNode root) {

        JsonNode paymentLink = root.path("payload")
                .path("payment_link")
                .path("entity");

        JsonNode payment = root.path("payload")
                .path("payment")
                .path("entity");

        String paymentLinkId = paymentLink.path("id").asText(null);
        String razorpayPaymentId = payment.path("id").asText(null);

        if (paymentLinkId == null) {
            throw new IllegalArgumentException("Payment Link ID missing");
        }
        if (razorpayPaymentId == null) {
            throw new IllegalArgumentException("Razorpay Payment ID missing");
        }

        System.out.println("Payment Link ID     : " + paymentLinkId);
        System.out.println("Razorpay Payment ID : " + razorpayPaymentId);

        paymentConfirmationService.confirmPayment(paymentLinkId, razorpayPaymentId);
    }



}
