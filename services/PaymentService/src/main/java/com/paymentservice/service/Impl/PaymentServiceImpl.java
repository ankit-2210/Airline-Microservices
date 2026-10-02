package com.paymentservice.service.Impl;

import com.airlineportal.payload.request.Payment.PaymentRequest;
import com.airlineportal.payload.response.Payment.PaymentResponse;
import com.airlineportal.utils.Booking.PaymentStatus;
import com.paymentservice.helper.PaymentHelper;
import com.paymentservice.mapper.PaymentMapper;
import com.paymentservice.model.Payment;
import com.paymentservice.repository.PaymentRepository;
import com.paymentservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentHelper paymentHelper;

    @Override
    @Transactional
    public PaymentResponse createPayment(PaymentRequest request) {
        paymentHelper.validatePaymentAmount(request.getAmount());
        paymentHelper.validatePaymentMethod(request.getPaymentMethod());

        if(paymentRepository.existsByBookingId(request.getBookingId())){
            throw new IllegalArgumentException("Payment already exists for booking: " + request.getBookingId());
        }

        Payment payment = PaymentMapper.toEntity(request);
        payment.setPaymentStatus(PaymentStatus.PENDING);
        payment.setTransactionId(generateTransactionId());
        payment.setCreatedAt(LocalDateTime.now());

        Payment saved = paymentRepository.save(payment);
        return PaymentMapper.toResponse(saved);
    }

    @Override
    public PaymentResponse getById(Long paymentId) {
        return PaymentMapper.toResponse(paymentHelper.findById(paymentId));
    }

    @Override
    public PaymentResponse getByBookingId(Long bookingId) {
        return PaymentMapper.toResponse(paymentHelper.findByBookingId(bookingId));
    }

    private String generateTransactionId() {
        return "TXN-" + UUID.randomUUID().toString()
                .replace("-", "")
                .substring(0, 20)
                .toUpperCase();
    }
}
