package com.paymentservice.internal;

import com.airlineportal.payload.response.ApiResponse;
import com.airlineportal.payload.response.Payment.PaymentResponse;
import com.paymentservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/payments")
public class InternalPaymentController {
    private final PaymentService paymentService;

    @GetMapping("/{paymentId}")
    public ApiResponse<PaymentResponse> getPaymentById(@PathVariable Long paymentId) {
        return ApiResponse.success(paymentService.getById(paymentId));
    }

    @GetMapping("/booking/{bookingId}")
    public ApiResponse<PaymentResponse> getPaymentByBookingId(@PathVariable Long bookingId) {
        return ApiResponse.success(paymentService.getByBookingId(bookingId));
    }



}
