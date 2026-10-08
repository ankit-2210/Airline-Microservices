package com.refundservice.external;


import com.airlineportal.payload.request.Payment.PaymentRefundRequest;
import com.airlineportal.payload.response.ApiResponse;
import com.airlineportal.payload.response.Booking.BookingResponse;
import com.airlineportal.payload.response.Flight.FlightInstanceResponse;
import com.airlineportal.payload.response.Payment.PaymentRefundResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface ExternalService {

    @GetMapping("/internal/bookings/{bookingId}")
    ApiResponse<BookingResponse> getBookingById(@PathVariable Long bookingId);


    @PostMapping("/internal/payments/refund")
    ApiResponse<PaymentRefundResponse> refundPayment(@RequestBody PaymentRefundRequest request);



}
