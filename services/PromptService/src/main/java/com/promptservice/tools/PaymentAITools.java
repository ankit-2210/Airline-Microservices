package com.promptservice.tools;

import com.airlineportal.client.PaymentFeignClient;
import com.airlineportal.payload.response.ApiResponse;
import com.airlineportal.payload.response.Payment.PaymentResponse;
import dev.langchain4j.agent.tool.Tool;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentAITools {
    private final PaymentFeignClient paymentFeignClient;

    @Tool("""
        Retrieve payment details using the payment ID.
        Use this when the user asks about a specific payment.
        The backend must verify that the payment belongs to the authenticated user.
        """)
    public ApiResponse<PaymentResponse> getPaymentById(Long paymentId) {
        return paymentFeignClient.getPaymentById(paymentId);
    }

    @Tool("""
        Retrieve payment details associated with a booking.
        Use this when the user asks whether a booking has been paid,
        or asks about the payment associated with a reservation.
        The backend must verify booking ownership.
        """)
    public ApiResponse<PaymentResponse> getPaymentByBookingId(Long bookingId) {
        return paymentFeignClient.getPaymentByBookingId(bookingId);
    }



}
