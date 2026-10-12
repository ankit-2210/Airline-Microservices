package com.adminservice.service.Impl;

import com.adminservice.service.AdminBookingService;
import com.airlineportal.client.BookingFeignClient;
import com.airlineportal.payload.response.ApiResponse;
import com.airlineportal.payload.response.Booking.BookingResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminBookingServiceImpl implements AdminBookingService {
    private final BookingFeignClient bookingFeignClient;

    @Override
    @Retry(name = "bookingRetry", fallbackMethod = "bookingFallback")
    @CircuitBreaker(name = "bookingCB", fallbackMethod = "bookingFallback")
    public ApiResponse<BookingResponse> getBookingById(Long bookingId) {
        return bookingFeignClient.getBookingById(bookingId);
    }

    @Override
    @Retry(name = "bookingRetry", fallbackMethod = "bookingFallback")
    @CircuitBreaker(name = "bookingCB", fallbackMethod = "bookingFallback")
    public ApiResponse<BookingResponse> getBookingByPnr(String pnr) {
        return bookingFeignClient.getBookingByPnr(pnr);
    }


    public ApiResponse<BookingResponse> bookingFallback(Long bookingId, Throwable ex) {
        return ApiResponse.failure("Booking service is currently unavailable.");
    }

    public ApiResponse<BookingResponse> bookingFallback(String pnr, Throwable ex) {
        return ApiResponse.failure("Booking service is currently unavailable.");
    }

}
