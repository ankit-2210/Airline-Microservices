package com.bookingservice.mapper;

import com.airlineportal.payload.request.Booking.PassengerRequest;
import com.airlineportal.payload.response.Booking.PassengerResponse;
import com.bookingservice.model.Passenger;

public final class PassengerMapper {
    private PassengerMapper(){

    }

    public static Passenger toEntity(PassengerRequest request){
        if(request == null)
            return null;

        return Passenger.builder()
                .firstName(request.getFirstName().trim())
                .lastName(request.getLastName().trim())
                .gender(request.getGender().trim().toUpperCase())
                .passportNumber(request.getPassportNumber())
                .seatNumber(request.getSeatNumber())
                .build();
    }

    public static PassengerResponse toResponse(Passenger passenger){
        if(passenger == null)
            return null;

        return PassengerResponse.builder()
                .id(passenger.getId())
                .firstName(passenger.getFirstName())
                .lastName(passenger.getLastName())
                .gender(passenger.getGender())
                .passportNumber(passenger.getPassportNumber())
                .seatNumber(passenger.getSeatNumber())
                .build();
    }




}
