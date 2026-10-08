package com.paymentservice.service;

import com.airlineportal.payload.request.Payment.PaymentRefundRequest;
import com.airlineportal.payload.response.Payment.PaymentResponse;
import com.airlineportal.payload.response.Payment.PaymentRefundResponse;

public interface PaymentService {

//    PaymentResponse createRazorpayOrder(Long paymentId);
//    PaymentResponse verifyPayment(Long paymentId, VerifyPaymentRequest request);


    PaymentResponse createPaymentLink(Long paymentId);
    PaymentResponse getById(Long paymentId);
    PaymentResponse getByBookingId(Long bookingId);

    PaymentRefundResponse refundPayment(PaymentRefundRequest request);
    
}
