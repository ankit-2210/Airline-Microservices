package com.paymentservice.controller;


import com.airlineportal.payload.response.ApiResponse;
import com.paymentservice.service.PaymentWebhookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments/webhook")
public class PaymentWebhookController {
    private final PaymentWebhookService paymentWebhookService;

    @PostMapping("/razorpay")
    public ApiResponse<String> handleRazorpayWebhook(@RequestHeader(name = "X-Razorpay-Signature", required = false) String signature, @RequestBody String payload){
        paymentWebhookService.handleWebhook(payload, signature);
        return ApiResponse.success("Webhook processed");

    }

}
