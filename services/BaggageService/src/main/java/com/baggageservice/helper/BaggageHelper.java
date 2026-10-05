package com.baggageservice.helper;

import com.airlineportal.exception.ResourceNotFoundException;
import com.airlineportal.payload.request.Baggage.BaggageCreateRequest;
import com.airlineportal.payload.response.Booking.BookingResponse;
import com.airlineportal.payload.response.Flight.FlightInstanceResponse;
import com.airlineportal.utils.Baggage.BaggageType;
import com.baggageservice.model.Baggage;
import com.baggageservice.repository.BaggageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.*;

@Component
@RequiredArgsConstructor
public class BaggageHelper {
    private final BaggageRepository baggageRepository;

    public Baggage findById(Long baggageId){
        if(baggageId == null){
            throw new IllegalArgumentException("Baggage id cannot be null");
        }

        return baggageRepository.findById(baggageId)
                .orElseThrow(() -> new ResourceNotFoundException("Baggage not found with id: " + baggageId));
    }


    public void validateCreateRequest(BaggageCreateRequest request){
        if(request == null){
            throw new IllegalArgumentException("Baggage request cannot be null");
        }
        if(request.getBookingId() == null){
            throw new IllegalArgumentException("Booking ID is required");
        }
        if(request.getPassengerId() == null){
            throw new IllegalArgumentException("Passenger ID is required");
        }
        if(request.getFlightInstanceId() == null){
            throw new IllegalArgumentException("Flight instance ID is required");
        }
        if(request.getBaggageType() == null){
            throw new IllegalArgumentException("Baggage type is required");
        }
        if(request.getWeight() == null || request.getWeight().compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("Weight must be greater than zero");
        }
        if(request.getQuantity() == null || request.getQuantity() <= 0){
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
    }

    public void validateBooking(BookingResponse booking, Long bookingId, Long passengerId, Long flightInstanceId){
        if(booking == null){
            throw new IllegalArgumentException("Booking not found: " + bookingId);
        }
        if(!booking.getId().equals(bookingId)){
            throw new IllegalArgumentException("Invalid booking");
        }
        if(!booking.getFlightInstanceId().equals(flightInstanceId)){
            throw new IllegalArgumentException("Flight instance does not belong to booking");
        }

        boolean passengerExists = booking.getPassengers().stream()
                .anyMatch(passenger -> passenger.getId().equals(passengerId));
        if(!passengerExists){
            throw new IllegalArgumentException("Passenger does not belong to booking");
        }
        if(!"CONFIRMED".equals(String.valueOf(booking.getBookingStatus()))){
            throw new IllegalArgumentException("Baggage can only be added to a confirmed booking");
        }

    }

    public void validateFlightInstance(FlightInstanceResponse flightInstance, Long requestedFlightInstanceId){
        if(flightInstance == null){
            throw new IllegalArgumentException("Flight instance not found: " + requestedFlightInstanceId);
        }
        if(!Boolean.TRUE.equals(flightInstance.getActive())){
            throw new IllegalArgumentException("Flight instance is inactive");
        }
    }

    public BigDecimal calculatePrice(BaggageType baggageType, BigDecimal weight, Integer quantity){
        BigDecimal pricePerKg;

        switch (baggageType){
            case CHECKED -> pricePerKg = new BigDecimal("100");
            case CABIN -> pricePerKg = new BigDecimal("50");
            case EXCESS -> pricePerKg = new BigDecimal("200");
            default -> throw new IllegalArgumentException("Unsupported baggage type");
        }

        return pricePerKg.multiply(weight)
                .multiply(BigDecimal.valueOf(quantity));
    }




}
