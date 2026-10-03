package com.paymentservice.service;

import com.paymentservice.external.RazorpayService;
import com.paymentservice.model.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentCallbackService {
    private final RazorpayService razorpayService;
    private final PaymentConfirmationService paymentConfirmationService;

    public Payment handlePaymentLinkCallback(String paymentId, String paymentLinkId, String referenceId, String status, String signature) {

        // 1. Verify Razorpay callback signature
        boolean valid = razorpayService.verifyPaymentLinkSignature(paymentLinkId, referenceId, status, paymentId, signature);
        if (!valid) {
            throw new IllegalArgumentException("Invalid Razorpay payment link signature");
        }

        // 2. Confirm status
        if (!"paid".equalsIgnoreCase(status)) {
            throw new IllegalArgumentException("Payment Link status is not paid: " + status);
        }

        // 3. Confirm payment
        return paymentConfirmationService.confirmPayment(paymentLinkId, paymentId);
    }

}
