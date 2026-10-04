package com.bookingservice.external;

import com.airlineportal.client.FareFeignClient;
import com.airlineportal.client.FlightFeignClient;
import com.airlineportal.client.SeatFeignClient;
import com.airlineportal.client.UserFeignClient;
import com.airlineportal.payload.request.Fare.FareQuoteRequest;
import com.airlineportal.payload.response.ApiResponse;
import com.airlineportal.payload.response.Fare.FareResponse;
import com.airlineportal.payload.response.Flight.FlightInstanceResponse;
import com.airlineportal.payload.response.Flight.FlightResponse;
import com.airlineportal.payload.response.Seat.SeatResponse;
import com.airlineportal.payload.response.User.UserResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExternalServiceImpl implements ExternalService{
    private final UserFeignClient userFeignClient;
    private final FlightFeignClient flightFeignClient;
    private final FareFeignClient fareFeignClient;
    private final SeatFeignClient seatFeignClient;

    // User
    @Override
    @Retry(name = "userRetry", fallbackMethod = "userFallback")
    @CircuitBreaker(name = "userCB", fallbackMethod = "userFallback")
    public ApiResponse<UserResponse> getUserById(Long userId) {
        return userFeignClient.getUserById(userId);
    }

    // Flight
    @Override
    @Retry(name = "flightRetry", fallbackMethod = "flightFallback")
    @CircuitBreaker(name = "flightCB", fallbackMethod = "flightFallback")
    public ApiResponse<FlightResponse> getFlightById(Long flightId) {
        return flightFeignClient.getFlightById(flightId);
    }

    // Flight Instance
    @Override
    @Retry(name = "flightRetry", fallbackMethod = "flightInstanceFallback")
    @CircuitBreaker(name = "flightCB", fallbackMethod = "flightInstanceFallback")
    public ApiResponse<FlightInstanceResponse> getInstanceById(Long instanceId) {
        return flightFeignClient.getInstanceById(instanceId);
    }

    @Override
    @Retry(name = "flightRetry", fallbackMethod = "reserveSeatsFallback")
    @CircuitBreaker(name = "flightCB", fallbackMethod = "reserveSeatsFallback")
    public ApiResponse<Boolean> reserveSeats(Long instanceId, Integer seats) {
        return flightFeignClient.reserveSeats(instanceId, seats);
    }

    @Override
    @Retry(name = "flightRetry", fallbackMethod = "releaseSeatsFallback")
    @CircuitBreaker(name = "flightCB", fallbackMethod = "releaseSeatsFallback")
    public ApiResponse<Boolean> releaseSeats(Long instanceId, Integer seats) {
        return flightFeignClient.releaseSeats(instanceId, seats);
    }

    @Override
    @Retry(name = "fareRetry", fallbackMethod = "fareFallback")
    @CircuitBreaker(name = "fareCB", fallbackMethod = "fareFallback")
    public ApiResponse<FareResponse> getFareQuote(FareQuoteRequest request) {
        return fareFeignClient.getFareQuote(request);
    }

    @Override
    @Retry(name = "seatRetry", fallbackMethod = "bookSeatFallback")
    @CircuitBreaker(name = "seatCB", fallbackMethod = "bookSeatFallback")
    public ApiResponse<SeatResponse> bookSeat(Long flightInstanceId, String seatNumber, Long bookingId) {
        return seatFeignClient.bookSeat(flightInstanceId, seatNumber, bookingId);
    }

    @Override
    @Retry(name = "seatRetry", fallbackMethod = "releaseSeatFallback")
    @CircuitBreaker(name = "seatCB", fallbackMethod = "releaseSeatFallback")
    public ApiResponse<SeatResponse> releaseSeat(Long flightInstanceId, String seatNumber) {
        return seatFeignClient.releaseSeat(flightInstanceId, seatNumber);
    }




    // FALLBACK METHODS
    public ApiResponse<UserResponse> userFallback(Long userId, Throwable t){
        return ApiResponse.failure("User Service is unavailable.");
    }

    public ApiResponse<FlightResponse> flightFallback(Long flightId, Throwable t){
        return ApiResponse.failure("Flight Service is unavailable.");
    }


    public ApiResponse<FlightInstanceResponse> flightInstanceFallback(Long instanceId, Throwable throwable){
        return ApiResponse.failure("Flight Service is currently unavailable.");
    }

    public ApiResponse<Boolean> reserveSeatsFallback(Long instanceId, Integer seats, Throwable throwable){
        return ApiResponse.failure("Flight Service is currently unavailable. " + "Seats could not be reserved.");
    }

    public ApiResponse<Boolean> releaseSeatsFallback(Long instanceId, Integer seats, Throwable throwable){
        return ApiResponse.failure("Flight Service is currently unavailable. " + "Seats could not be released.");
    }

    public ApiResponse<FareResponse> fareFallback(FareQuoteRequest request, Throwable t){
        return ApiResponse.failure("Fare Service is unavailable.");
    }

    public ApiResponse<SeatResponse> bookSeatFallback(Long flightInstanceId, String seatNumber, Long bookingId, Throwable throwable){
        return ApiResponse.failure("Seat Service is unavailable. Seat could not be booked.");
    }

    public ApiResponse<SeatResponse> releaseSeatFallback(Long flightInstanceId, String seatNumber, Throwable throwable){
        return ApiResponse.failure("Seat Service is unavailable. Seat could not be released.");
    }




}
