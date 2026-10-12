package com.adminservice.service.Impl;

import com.adminservice.service.AdminPaymentService;
import com.airlineportal.client.PaymentFeignClient;
import com.airlineportal.payload.response.ApiResponse;
import com.airlineportal.payload.response.Payment.PaymentResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminPaymentServiceImpl implements AdminPaymentService {
    private final PaymentFeignClient paymentFeignClient;

    @Override
    @Retry(name = "paymentRetry", fallbackMethod = "paymentFallback")
    @CircuitBreaker(name = "paymentCB", fallbackMethod = "paymentFallback")
    public ApiResponse<PaymentResponse> getPaymentById(Long paymentId) {
        return paymentFeignClient.getPaymentById(paymentId);
    }

    @Override
    @Retry(name = "paymentRetry", fallbackMethod = "paymentFallback")
    @CircuitBreaker(name = "paymentCB", fallbackMethod = "paymentFallback")
    public ApiResponse<PaymentResponse> getPaymentByBookingId(Long bookingId) {
        return paymentFeignClient.getPaymentByBookingId(bookingId);
    }



    public ApiResponse<PaymentResponse> paymentFallback(Long id, Throwable ex) {
        return ApiResponse.failure("Payment service is currently unavailable.");
    }
}
