package com.checkinservice.external;


import com.airlineportal.client.BookingFeignClient;
import com.airlineportal.client.FlightFeignClient;
import com.airlineportal.client.SeatFeignClient;
import com.airlineportal.payload.response.ApiResponse;
import com.airlineportal.payload.response.Booking.BookingResponse;
import com.airlineportal.payload.response.Flight.FlightInstanceResponse;
import com.airlineportal.payload.response.Seat.SeatResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExternalServiceImpl implements ExternalService{
    private final FlightFeignClient flightFeignClient;
    private final BookingFeignClient bookingFeignClient;
    private final SeatFeignClient seatFeignClient;

    @Override
    @Retry(name = "bookingRetry", fallbackMethod = "bookingFallback")
    @CircuitBreaker(name = "bookingCB", fallbackMethod = "bookingFallback")
    public ApiResponse<BookingResponse> getBookingById(Long bookingId) {
        return bookingFeignClient.getBookingById(bookingId);
    }

    @Override
    @Retry(name = "bookingRetry", fallbackMethod = "bookingsByUserFallback")
    @CircuitBreaker(name = "bookingCB", fallbackMethod = "bookingsByUserFallback")
    public ApiResponse<Page<BookingResponse>> getBookingsByUser(Long userId, Pageable pageable) {
        return bookingFeignClient.getBookingsByUser(userId, pageable);
    }

    // Flight Instance
    @Override
    @Retry(name = "flightRetry", fallbackMethod = "flightInstanceFallback")
    @CircuitBreaker(name = "flightCB", fallbackMethod = "flightInstanceFallback")
    public ApiResponse<FlightInstanceResponse> getInstanceById(Long instanceId) {
        return flightFeignClient.getInstanceById(instanceId);
    }

    @Override
    @Retry(name = "seatRetry", fallbackMethod = "seatFallback")
    @CircuitBreaker(name = "seatCB", fallbackMethod = "seatFallback")
    public ApiResponse<SeatResponse> getSeat(Long flightInstanceId, String seatNumber) {
        return seatFeignClient.getSeat(flightInstanceId, seatNumber);
    }


    // Fallback Method

    public ApiResponse<BookingResponse> bookingFallback(Long bookingId, Throwable throwable){
        return ApiResponse.failure("Booking Service is currently unavailable.");
    }

    public ApiResponse<Page<BookingResponse>> bookingsByUserFallback(Long userId, Pageable pageable, Throwable throwable) {
        return ApiResponse.failure("Booking Service is currently unavailable.");
    }

    public ApiResponse<FlightInstanceResponse> flightInstanceFallback(Long instanceId, Throwable throwable){
        return ApiResponse.failure("Flight Service is currently unavailable.");
    }

    public ApiResponse<SeatResponse> seatFallback(Long flightInstanceId, String seatNumber, Throwable throwable){
        return ApiResponse.failure("Seat Service is currently unavailable.");
    }



}
