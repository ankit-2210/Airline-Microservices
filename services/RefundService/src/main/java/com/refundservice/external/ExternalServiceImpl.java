package com.refundservice.external;


import com.airlineportal.client.BookingFeignClient;
import com.airlineportal.client.FlightFeignClient;
import com.airlineportal.client.PaymentFeignClient;
import com.airlineportal.payload.request.Payment.PaymentRefundRequest;
import com.airlineportal.payload.response.ApiResponse;
import com.airlineportal.payload.response.Booking.BookingResponse;
import com.airlineportal.payload.response.Flight.FlightInstanceResponse;
import com.airlineportal.payload.response.Payment.PaymentRefundResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExternalServiceImpl implements ExternalService{
    private final BookingFeignClient bookingFeignClient;
    private final PaymentFeignClient paymentFeignClient;

    @Override
    @Retry(name = "bookingRetry", fallbackMethod = "bookingFallback")
    @CircuitBreaker(name = "bookingCB", fallbackMethod = "bookingFallback")
    public ApiResponse<BookingResponse> getBookingById(Long bookingId) {
        return bookingFeignClient.getBookingById(bookingId);
    }

    @Override
    @Retry(name = "paymentRefundRetry", fallbackMethod = "paymentRefundFallback")
    @CircuitBreaker(name = "paymentRefundCB", fallbackMethod = "paymentRefundFallback")
    public ApiResponse<PaymentRefundResponse> refundPayment(PaymentRefundRequest request) {
        return paymentFeignClient.refundPayment(request);
    }


    // Fallback Method

    public ApiResponse<BookingResponse> bookingFallback(Long bookingId, Throwable throwable){
        return ApiResponse.failure("Booking Service is currently unavailable.");
    }

    public ApiResponse<PaymentRefundResponse> paymentRefundFallback(PaymentRefundRequest request, Throwable throwable){
        return ApiResponse.failure("Payment Service is currently unavailable.");
    }





}
