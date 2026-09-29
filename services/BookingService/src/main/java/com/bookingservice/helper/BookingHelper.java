package com.bookingservice.helper;

import com.airlineportal.exception.ResourceNotFoundException;
import com.airlineportal.payload.request.Booking.BookingRequest;
import com.airlineportal.payload.request.Booking.PassengerRequest;
import com.airlineportal.utils.Booking.BookingStatus;
import com.airlineportal.utils.Booking.PaymentStatus;
import com.bookingservice.model.Booking;
import com.bookingservice.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.*;

@Component
@RequiredArgsConstructor
public class BookingHelper {
    private final BookingRepository bookingRepository;

    public Booking findById(Long bookingId){
        if(bookingId == null){
            throw new IllegalArgumentException("Booking id cannot be null");
        }

        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + bookingId));
    }

    public Booking findByPnr(String pnr) {
        String normalizedPnr = normalizePnr(pnr);

        return bookingRepository.findByPnr(normalizedPnr)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with PNR: " + normalizedPnr));
    }


    public Booking findByIdAndUser(Long bookingId, Long userId){
        if(bookingId == null){
            throw new IllegalArgumentException("Booking id cannot be null");
        }
        if(userId == null){
            throw new IllegalArgumentException("User id cannot be null");
        }

        return bookingRepository.findByIdAndUserId(bookingId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found or you are not authorized"));
    }

    public void validateCreate(BookingRequest request){
        if(request == null){
            throw new IllegalArgumentException("Booking request cannot be null");
        }
        if(request.getUserId() == null){
            throw new IllegalArgumentException("User id cannot be null");
        }
        if(request.getFlightInstanceId() == null){
            throw new IllegalArgumentException("Flight id cannot be null");
        }
        if(request.getPassengers() == null || request.getPassengers().isEmpty()){
            throw new IllegalArgumentException("At least one passenger is required");
        }

        validatePassengers(request.getPassengers());
    }

    private void validatePassengers(List<PassengerRequest> passengers){
        Set<String> seatNumbers = new HashSet<>();
        if(passengers == null || passengers.isEmpty()){
            throw new IllegalArgumentException("At least one passenger is required");
        }

        for(PassengerRequest passenger: passengers){
           if(passenger == null){
               throw new IllegalArgumentException("Passenger cannot be null");
           }

           validatePassenger(passenger);
        }
    }

    private void validatePassenger(PassengerRequest passenger){
        if(passenger.getFirstName() == null || passenger.getFirstName().isBlank()){
            throw new IllegalArgumentException("Passenger first name cannot be blank");
        }
        if(passenger.getLastName() == null || passenger.getLastName().isBlank()){
            throw new IllegalArgumentException("Passenger last name cannot be blank");
        }
        if(passenger.getGender() == null || passenger.getGender().isBlank()){
            throw new IllegalArgumentException("Passenger gender cannot be blank");
        }
        if(passenger.getPassportNumber() != null && passenger.getPassportNumber().isBlank()){
            throw new IllegalArgumentException("Passport number cannot be blank");
        }
        if (passenger.getSeatNumber() != null && passenger.getSeatNumber().isBlank()){
            passenger.setSeatNumber(null);
        }
    }

    public String generateUniquePnr(){
        String pnr;
        do {
            pnr = UUID.randomUUID().toString()
                    .replace("-", "")
                    .substring(0, 10)
                    .toUpperCase(Locale.ROOT);
        } while (bookingRepository.existsByPnr(pnr));

        return pnr;
    }

    public String normalizePnr(String pnr){
        if(!StringUtils.hasText(pnr)){
            throw new IllegalArgumentException("PNR cannot be blank");
        }

        return pnr.trim().toUpperCase();
    }


    public void validateStatus(BookingStatus bookingStatus){
        if(bookingStatus == null){
            return;
        }

        // Keep transition rules here.
    }


    public void validatePaymentStatus(PaymentStatus paymentStatus){
        if(paymentStatus == null){
            return;
        }

        // Keep payment transition rules here.
    }


    public void validateCanCancel(Booking booking){
        if(booking == null){
            throw new ResourceNotFoundException("Booking not found");
        }

        if(BookingStatus.CANCELLED.equals(booking.getBookingStatus())){
            throw new IllegalArgumentException("Booking is already cancelled");
        }
    }




}
