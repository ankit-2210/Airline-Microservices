package com.flightservice.mapper;

import com.airlineportal.payload.request.Flight.FlightRequest;
import com.airlineportal.payload.response.Flight.FlightResponse;
import com.flightservice.model.Flight;


public class FlightMapper {
    private FlightMapper(){

    }

    public static Flight toEntity(FlightRequest request){
        if(request == null)
            return null;

        return Flight.builder()
                .flightNumber(request.getFlightNumber().trim().toUpperCase())

                .airlineId(request.getAirlineId())
                .aircraftId(request.getAircraftId())

                .departureAirportId(request.getDepartureAirportId())
                .arrivalAirportId(request.getArrivalAirportId())

                .scheduledDeparture(request.getScheduledDeparture())
                .scheduledArrival(request.getScheduledArrival())

                .actualDeparture(request.getActualDeparture())
                .actualArrival(request.getActualArrival())

                .flightStatus(request.getFlightStatus())

                .active(request.getActive() != null ? request.getActive() : true)
                .build();
    }


    public static void updateEntity(Flight flight, FlightRequest request){
        flight.setFlightNumber(request.getFlightNumber().trim().toUpperCase());

        flight.setAircraftId(request.getAircraftId());

        flight.setDepartureAirportId(request.getDepartureAirportId());
        flight.setArrivalAirportId(request.getArrivalAirportId());

        flight.setScheduledArrival(request.getScheduledArrival());
        flight.setScheduledDeparture(request.getScheduledDeparture());

        flight.setActualDeparture(request.getActualDeparture());
        flight.setActualArrival(request.getActualArrival());

        flight.setFlightStatus(request.getFlightStatus());

        if(request.getActive() != null) {
            flight.setActive(request.getActive());
        }
    }

    public static FlightResponse toResponse(Flight flight){
        return FlightResponse.builder()
                .id(flight.getId())

                .flightNumber(flight.getFlightNumber())

                .scheduledArrival(flight.getScheduledArrival())
                .scheduledDeparture(flight.getScheduledDeparture())

                .actualDeparture(flight.getActualDeparture())
                .actualArrival(flight.getActualArrival())

                .flightStatus(flight.getFlightStatus())
                .active(flight.getActive())

                .delayed(flight.getDelayed())
                .delayMinutes(flight.getDelayMinutes())

                .createdAt(flight.getCreatedAt())
                .updatedAt(flight.getUpdatedAt())
                .build();
    }

}
