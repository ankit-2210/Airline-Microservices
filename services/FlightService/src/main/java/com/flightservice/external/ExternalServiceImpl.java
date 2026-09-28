package com.flightservice.external;

import com.airlineportal.client.AirlineFeignClient;
import com.airlineportal.client.LocationFeignClient;
import com.airlineportal.payload.response.Airlines.Aircraft.AircraftResponse;
import com.airlineportal.payload.response.Airlines.Airline.AirlineResponse;
import com.airlineportal.payload.response.ApiResponse;
import com.airlineportal.payload.response.Location.Airport.AirportResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExternalServiceImpl implements ExternalService{
    private final AirlineFeignClient airlineFeignClient;
    private final LocationFeignClient locationFeignClient;

    @Override
    @Retry(name = "airlineRetry", fallbackMethod = "airlineFallback")
    @CircuitBreaker(name = "airlineCB", fallbackMethod = "airlineFallback")
    public ApiResponse<AirlineResponse> getAirlineById(Long airlineId) {
        return airlineFeignClient.getAirlineById(airlineId);
    }

    @Override
    @Retry(name = "aircraftRetry", fallbackMethod = "aircraftFallback")
    @CircuitBreaker(name = "aircraftCB", fallbackMethod = "aircraftFallback")
    public ApiResponse<AircraftResponse> getAircraftById(Long aircraftId) {
        return airlineFeignClient.getAircraftById(aircraftId);
    }

    @Override
    @Retry(name = "locationRetry", fallbackMethod = "locationFallback")
    @CircuitBreaker(name = "locationCB", fallbackMethod = "locationFallback")
    public ApiResponse<AirportResponse> getAirportById(Long airportId) {
        return locationFeignClient.getAirportById(airportId);
    }

    @Override
    @Retry(name = "aircraftRetry", fallbackMethod = "aircraftOwnershipFallback")
    @CircuitBreaker(name = "aircraftCB", fallbackMethod = "aircraftOwnershipFallback")
    public ApiResponse<Boolean> aircraftBelongsToAirline(Long aircraftId, Long airlineId) {
        return airlineFeignClient.aircraftBelongsToAirline(aircraftId, airlineId);
    }


    // Fallback Methods
    public AirlineResponse airlineFallback(Long airlineId, Throwable throwable){
        throw new IllegalStateException("Airline Service is unavailable.");
    }

    public AircraftResponse aircraftFallback(Long aircraftId, Throwable throwable){
        throw new IllegalStateException("Airline Service is unavailable.");
    }

    public AirportResponse locationFallback(Long airportId, Throwable throwable){
        throw new IllegalStateException("Location Service is unavailable.");
    }

    public boolean aircraftOwnershipFallback(Long aircraftId, Long airlineId, Throwable throwable){
        throw new IllegalStateException("Airline Service is unavailable.");
    }


}
