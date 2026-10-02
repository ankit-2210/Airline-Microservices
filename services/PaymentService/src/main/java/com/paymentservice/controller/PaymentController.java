package com.paymentservice.controller;

import com.airlineportal.payload.request.Payment.PaymentRequest;
import com.airlineportal.payload.response.ApiResponse;
import com.airlineportal.payload.response.Payment.PaymentResponse;
import com.paymentservice.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping
    public ApiResponse<PaymentResponse> createPayment(@Valid @RequestBody PaymentRequest request){
        return ApiResponse.success(paymentService.createPayment(request));
    }

    @GetMapping("/{paymentId}")
    public ApiResponse<PaymentResponse> getById(@PathVariable Long paymentId){
        return ApiResponse.success(paymentService.getById(paymentId));
    }

    @GetMapping("/booking/{bookingId}")
    public ApiResponse<PaymentResponse> getByBookingId(@PathVariable Long bookingId){
        return ApiResponse.success(paymentService.getByBookingId(bookingId));
    }



}
