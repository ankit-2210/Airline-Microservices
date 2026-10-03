package com.airlineportal.client;


import com.airlineportal.payload.response.ApiResponse;
import com.airlineportal.payload.response.Payment.PaymentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;


@FeignClient(
        name = "PAYMENTSERVICE",
        path = "/internal"
)
public interface PaymentFeignClient {

    @GetMapping("/{paymentId}")
    ApiResponse<PaymentResponse> getPaymentById(@PathVariable Long paymentId);

    @GetMapping("/booking/{bookingId}")
    ApiResponse<PaymentResponse> getPaymentByBookingId(@PathVariable Long bookingId);




}
