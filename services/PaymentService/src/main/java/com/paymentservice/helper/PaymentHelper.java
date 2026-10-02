package com.paymentservice.helper;

import com.airlineportal.exception.ResourceNotFoundException;
import com.airlineportal.utils.Booking.PaymentStatus;
import com.paymentservice.model.Payment;
import com.paymentservice.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.*;

@Component
@RequiredArgsConstructor
public class PaymentHelper {
    private final PaymentRepository paymentRepository;

    public Payment findById(Long paymentId){
        if(paymentId == null){
            throw new IllegalArgumentException("Payment id cannot be null");
        }

        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + paymentId));
    }

    public Payment findByBookingId(Long bookingId){
        if(bookingId == null){
            throw new IllegalArgumentException("Booking id cannot be null");
        }

        return paymentRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found for booking: " + bookingId));
    }

    public void validateAmount(BigDecimal amount){
        if(amount == null){
            throw new IllegalArgumentException("Payment amount cannot be null");
        }
        if(amount.compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("Payment amount must be greater than zero");
        }
    }

    public void validateCanCreateOrder(Payment payment){
        if(payment == null){
            throw new IllegalArgumentException("Payment cannot be null");
        }

        validateAmount(payment.getAmount());
        if(payment.getPaymentStatus() == PaymentStatus.SUCCESS){
            throw new IllegalArgumentException("Payment is already completed");
        }

        if(StringUtils.hasText(payment.getRazorpayOrderId())){
            throw new IllegalArgumentException("Razorpay order already exists");
        }
    }

    public void validateOrder(Payment payment, String razorpayOrderId){
        if(payment == null){
            throw new IllegalArgumentException("Payment cannot be null");
        }

        if(!StringUtils.hasText(payment.getRazorpayOrderId())){
            throw new IllegalArgumentException("Razorpay order has not been created");
        }

        if(!payment.getRazorpayOrderId().equals(razorpayOrderId)){
            throw new IllegalArgumentException("Razorpay order does not belong to this payment");
        }

    }

}
