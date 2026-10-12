package com.adminservice.service;

import com.airlineportal.payload.response.ApiResponse;
import com.airlineportal.payload.response.Payment.PaymentResponse;

public interface AdminPaymentService {
    ApiResponse<PaymentResponse> getPaymentById(Long paymentId);

    ApiResponse<PaymentResponse> getPaymentByBookingId(Long bookingId);

}
