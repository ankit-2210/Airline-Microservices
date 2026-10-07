package com.paymentservice.service;

import com.airlineportal.payload.request.Payment.PaymentRequest;
import com.airlineportal.payload.response.Payment.PaymentResponse;
import com.paymentservice.dto.request.VerifyPaymentRequest;

public interface PaymentService {

//    PaymentResponse createRazorpayOrder(Long paymentId);
//    PaymentResponse verifyPayment(Long paymentId, VerifyPaymentRequest request);


    PaymentResponse createPaymentLink(Long paymentId);
    PaymentResponse getById(Long paymentId);
    PaymentResponse getByBookingId(Long bookingId);


    
}
