package com.checkinservice.helper;

import com.airlineportal.exception.ResourceNotFoundException;
import com.airlineportal.payload.request.CheckIn.CheckInRequest;
import com.airlineportal.payload.response.Booking.BookingResponse;
import com.airlineportal.payload.response.Flight.FlightInstanceResponse;
import com.airlineportal.payload.response.Seat.SeatResponse;
import com.airlineportal.utils.CheckIn.CheckInStatus;
import com.checkinservice.model.CheckIn;
import com.checkinservice.repository.CheckInRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;


@Component
@RequiredArgsConstructor
public class CheckInHelper {
    private final CheckInRepository checkInRepository;

    // ==================== FIND ====================
    public CheckIn findById(Long checkInId) {
        if (checkInId == null) {
            throw new IllegalArgumentException("Check-in id cannot be null");
        }

        return checkInRepository.findById(checkInId)
                .orElseThrow(() -> new ResourceNotFoundException("Check-in not found with id: " + checkInId));
    }

    public CheckIn findByBookingAndPassenger(Long bookingId, Long passengerId){
        validateIds(bookingId, passengerId);

        return checkInRepository.findByBookingIdAndPassengerId(bookingId, passengerId)
                .orElseThrow(() -> new ResourceNotFoundException("Check-in not found for booking: " + bookingId + " and passenger: " + passengerId));
    }


    // ==================== REQUEST VALIDATION ====================

    public void validateCheckIn(CheckInRequest request) {
        if(request == null){
            throw new IllegalArgumentException("Check-in request cannot be null");
        }
        if(request.getBookingId() == null){
            throw new IllegalArgumentException("Booking id cannot be null");
        }
        if(request.getPassengerId() == null){
            throw new IllegalArgumentException("Passenger id cannot be null");
        }
    }


    private void validateIds(Long bookingId, Long passengerId){
        if(bookingId == null){
            throw new IllegalArgumentException("Booking id cannot be null");
        }
        if(passengerId == null){
            throw new IllegalArgumentException("Passenger id cannot be null");
        }
    }


    // ==================== BOOKING VALIDATION ====================

    public void validatePassenger(BookingResponse booking, Long passengerId){
        if(booking == null){
            throw new ResourceNotFoundException("Booking not found");
        }
        if(booking.getPassengers() == null || booking.getPassengers().isEmpty()){
            throw new IllegalArgumentException("Booking has no passengers");
        }

        boolean passengerExists = booking.getPassengers().stream()
                .anyMatch(passenger -> passengerId.equals(passenger.getId()));
        if(!passengerExists){
            throw new IllegalArgumentException("Passenger does not belong to this booking");
        }
    }


    public void validateBookingForCheckIn(BookingResponse booking){
        if(booking == null){
            throw new ResourceNotFoundException("Booking not found");
        }
        if (booking.getBookingStatus() == null) {
            throw new IllegalArgumentException("Booking status is not available");
        }
        if (!"CONFIRMED".equalsIgnoreCase(booking.getBookingStatus())){
            throw new IllegalArgumentException("Check-in is allowed only for confirmed bookings");
        }

    }


    // ==================== FLIGHT VALIDATION ====================

    public void validateFlightForCheckIn(FlightInstanceResponse flightInstance){
        if(flightInstance == null){
            throw new ResourceNotFoundException("Flight instance not found");
        }

        if(Boolean.FALSE.equals(flightInstance.getActive())){
            throw new IllegalArgumentException("Flight instance is not active");
        }
        if(Boolean.FALSE.equals(flightInstance.getCanCheckIn())){
            throw new IllegalArgumentException("Check-in is not currently available");
        }
    }


    // ==================== PASSENGER SEAT ====================
    public String getPassengerSeat(BookingResponse booking, Long passengerId){
        if(booking.getPassengers() == null){
            throw new IllegalArgumentException("Booking has no passengers");
        }

        return booking.getPassengers().stream()
                .filter(passenger -> passengerId.equals(passenger.getId()))
                .findFirst()
                .map(passenger -> {
                    if(!StringUtils.hasText(passenger.getSeatNumber())){
                        throw new IllegalArgumentException("Passenger does not have an assigned seat");
                    }
                    return passenger.getSeatNumber().trim().toUpperCase();
                })
                .orElseThrow(() -> new ResourceNotFoundException("Passenger not found in booking"));
    }


    // ==================== SEAT VALIDATION ====================
    public void validateSeatForCheckIn(SeatResponse seat, Long bookingId, String seatNumber){
        if(seat == null){
            throw new ResourceNotFoundException("Seat not found: " + seatNumber);
        }
        if(!bookingId.equals(seat.getBookingId())){
            throw new IllegalArgumentException("Seat does not belong to this booking");
        }
        if(!seatNumber.equalsIgnoreCase(seat.getSeatNumber())){
            throw new IllegalArgumentException("Invalid seat number");
        }
        if (seat.getSeatStatus() == null) {
            throw new IllegalArgumentException("Seat status is not available");
        }
        if(!"BOOKED".equalsIgnoreCase(seat.getSeatStatus().toString())){
            throw new IllegalArgumentException("Seat is not booked");
        }
    }


    // ==================== CANCEL ====================

    public void validateCanCancel(CheckIn checkIn){
        if (checkIn == null) {
            throw new ResourceNotFoundException("Check-in not found");
        }

        if (CheckInStatus.CANCELLED.equals(checkIn.getStatus())){
            throw new IllegalArgumentException("Check-in is already cancelled");
        }
    }




}
