package com.paymentservice.internal;

import com.airlineportal.payload.response.ApiResponse;
import com.airlineportal.payload.response.Payment.PaymentResponse;
import com.airlineportal.payload.request.Payment.PaymentRefundRequest;
import com.airlineportal.payload.response.Payment.PaymentRefundResponse;
import com.paymentservice.service.PaymentService;
import jakarta.validation.Valid;
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

    @PostMapping("/refund")
    public ApiResponse<PaymentRefundResponse> refundPayment(@Valid @RequestBody PaymentRefundRequest request){
        return ApiResponse.success(paymentService.refundPayment(request));
    }




}
