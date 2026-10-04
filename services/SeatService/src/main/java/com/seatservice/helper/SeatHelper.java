package com.seatservice.helper;

import com.airlineportal.exception.ResourceNotFoundException;
import com.airlineportal.payload.request.Seat.SeatCreateRequest;
import com.airlineportal.utils.Seat.SeatStatus;
import com.seatservice.model.Seat;
import com.seatservice.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SeatHelper {
    private final SeatRepository seatRepository;

    public Seat findById(Long seatId) {
        if(seatId == null) {
            throw new IllegalArgumentException("Seat id cannot be null");
        }

        return seatRepository.findById(seatId)
                .orElseThrow(() -> new ResourceNotFoundException("Seat not found with id: " + seatId));

    }

    public Seat findByInstanceAndNumber(Long flightInstanceId, String seatNumber){
        return seatRepository.findByFlightInstanceIdAndSeatNumber(flightInstanceId, normalizeSeatNumber(seatNumber))
                .orElseThrow(() -> new ResourceNotFoundException("Seat not found: " + seatNumber));
    }

    public void validateCreate(SeatCreateRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Seat request cannot be null");
        }

        if (request.getFlightInstanceId() == null) {
            throw new IllegalArgumentException("Flight instance id cannot be null");
        }

        if (request.getSeatNumber() == null || request.getSeatNumber().isBlank()){
            throw new IllegalArgumentException("Seat number cannot be blank");
        }

        if (request.getSeatClass() == null) {
            throw new IllegalArgumentException("Seat class cannot be null");
        }

        String seatNumber = normalizeSeatNumber(request.getSeatNumber());
        if(seatRepository.existsByFlightInstanceIdAndSeatNumber(request.getFlightInstanceId(), seatNumber)){
            throw new IllegalArgumentException("Seat already exists: " + seatNumber);
        }

    }

    public void validateAvailable(Seat seat) {
        if (seat.getSeatStatus() != SeatStatus.AVAILABLE) {
            throw new IllegalArgumentException("Seat " + seat.getSeatNumber() + " is not available");
        }

    }

    public String normalizeSeatNumber(String seatNumber) {
        if(seatNumber == null)
            return null;

        return seatNumber.trim().toUpperCase();
    }

}
