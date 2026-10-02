package com.paymentservice.service.Impl;

import com.airlineportal.event.Payment.PaymentCompletedEvent;
import com.airlineportal.event.Payment.PaymentFailedEvent;
import com.airlineportal.payload.response.Payment.PaymentResponse;
import com.airlineportal.utils.Booking.PaymentStatus;
import com.paymentservice.dto.request.VerifyPaymentRequest;
import com.paymentservice.dto.response.RazorpayOrderResponse;
import com.paymentservice.event.PaymentEventPublisher;
import com.paymentservice.external.RazorpayService;
import com.paymentservice.helper.PaymentHelper;
import com.paymentservice.mapper.PaymentMapper;
import com.paymentservice.model.Payment;
import com.paymentservice.repository.PaymentRepository;
import com.paymentservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentHelper paymentHelper;
    private final RazorpayService razorpayService;
    private final PaymentEventPublisher paymentEventPublisher;

    @Override
    @Transactional
    public PaymentResponse createRazorpayOrder(Long paymentId) {
        Payment payment = paymentHelper.findById(paymentId);

        paymentHelper.validateCanCreateOrder(payment);
        long amountInPaise = payment.getAmount().multiply(BigDecimal.valueOf(100)).longValueExact();

        RazorpayOrderResponse order = razorpayService.createOrder(amountInPaise, "BOOKING-" + payment.getBookingId());
        if (order == null || order.getId() == null) {
            throw new IllegalStateException("Unable to create Razorpay order");
        }

        payment.setRazorpayOrderId(order.getId());
        payment.setUpdatedAt(LocalDateTime.now());

        Payment saved = paymentRepository.save(payment);
        PaymentResponse response = PaymentMapper.toResponse(saved);

        response.setRazorpayKeyId(razorpayService.getKeyId());
        return response;
    }

    @Override
    @Transactional
    public PaymentResponse verifyPayment(Long paymentId, VerifyPaymentRequest request) {
        Payment payment = paymentHelper.findById(paymentId);

        paymentHelper.validateOrder(payment, request.getRazorpayOrderId());

        boolean verified = razorpayService.verifyPaymentSignature(request.getRazorpayOrderId(), request.getRazorpayPaymentId(), request.getRazorpaySignature());
        if (!verified) {
            payment.setPaymentStatus(PaymentStatus.FAILED);
            payment.setUpdatedAt(LocalDateTime.now());

            Payment failed = paymentRepository.save(payment);
            publishPaymentFailed(failed, "Invalid Razorpay signature");
            throw new IllegalArgumentException("Invalid Razorpay payment signature");
        }

        payment.setRazorpayPaymentId(request.getRazorpayPaymentId());
        payment.setRazorpaySignature(request.getRazorpaySignature());
        payment.setTransactionId(request.getRazorpayPaymentId());
        payment.setPaymentStatus(PaymentStatus.SUCCESS);
        payment.setPaidAt(LocalDateTime.now());
        payment.setUpdatedAt(LocalDateTime.now());

        Payment saved = paymentRepository.save(payment);

        publishPaymentCompleted(saved);
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


    // PUBLISH PAYMENT COMPLETED
    private void publishPaymentCompleted(Payment payment){
        PaymentCompletedEvent event = PaymentCompletedEvent.builder()
                        .paymentId(payment.getId())
                        .bookingId(payment.getBookingId())
                        .userId(payment.getUserId())

                        .pnr(payment.getPnr())
                        .amount(payment.getAmount())

                        .razorpayOrderId(payment.getRazorpayOrderId())
                        .razorpayPaymentId(payment.getRazorpayPaymentId())

                        .occurredAt(LocalDateTime.now())
                        .build();

        paymentEventPublisher.publishPaymentCompleted(event);
    }


    // PUBLISH FAILURE EVENT
    private void publishPaymentFailed(Payment payment, String reason){
        PaymentFailedEvent event = PaymentFailedEvent.builder()
                        .paymentId(payment.getId())
                        .bookingId(payment.getBookingId())
                        .userId(payment.getUserId())

                        .pnr(payment.getPnr())
                        .amount(payment.getAmount())

                        .razorpayOrderId(payment.getRazorpayOrderId())

                        .reason(reason)
                        .occurredAt(LocalDateTime.now())
                        .build();

        paymentEventPublisher.publishPaymentFailed(event);
    }


}
