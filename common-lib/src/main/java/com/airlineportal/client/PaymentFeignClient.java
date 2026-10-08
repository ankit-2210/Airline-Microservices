package com.airlineportal.client;


import com.airlineportal.payload.request.Payment.PaymentRefundRequest;
import com.airlineportal.payload.response.ApiResponse;
import com.airlineportal.payload.response.Payment.PaymentRefundResponse;
import com.airlineportal.payload.response.Payment.PaymentResponse;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;


@FeignClient(
        name = "PAYMENTSERVICE",
        path = "/internal/payments"
)
public interface PaymentFeignClient {

    @GetMapping("/{paymentId}")
    ApiResponse<PaymentResponse> getPaymentById(@PathVariable Long paymentId);

    @GetMapping("/booking/{bookingId}")
    ApiResponse<PaymentResponse> getPaymentByBookingId(@PathVariable Long bookingId);

    @PostMapping("/refund")
    ApiResponse<PaymentRefundResponse> refundPayment(@Valid @RequestBody PaymentRefundRequest request);


}
