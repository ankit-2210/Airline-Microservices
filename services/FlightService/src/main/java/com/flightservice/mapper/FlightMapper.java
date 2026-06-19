package com.flightservice.mapper;

import com.flightservice.model.Flight;
import com.microservices.payload.request.Flight.FlightRequest;
import com.microservices.payload.response.Flight.FlightResponse;

public class FlightMapper {
    public static Flight toEntity(FlightRequest flightRequest){
        return Flight.builder()
                .flightNumber(flightRequest.getFlightNumber().toUpperCase())

                .airlineId(flightRequest.getAirlineId())
                .aircraftId(flightRequest.getAircraftId())

                .departureAirportId(flightRequest.getDepartureAirportId())
                .arrivalAirportId(flightRequest.getArrivalAirportId())

                .scheduledDeparture(flightRequest.getScheduledDeparture())
                .scheduledArrival(flightRequest.getScheduledArrival())

                .actualDeparture(flightRequest.getActualDeparture())
                .actualArrival(flightRequest.getActualArrival())

                .flightStatus(flightRequest.getFlightStatus())

                .active(flightRequest.getActive() != null ? flightRequest.getActive() : true)
                .build();
    }

    public static void updateEntity(Flight flight, FlightRequest flightRequest){
        flight.setFlightNumber(flightRequest.getFlightNumber().toUpperCase());

        flight.setAircraftId(flightRequest.getAircraftId());

        flight.setDepartureAirportId(flightRequest.getDepartureAirportId());
        flight.setArrivalAirportId(flightRequest.getArrivalAirportId());

        flight.setScheduledArrival(flightRequest.getScheduledArrival());
        flight.setScheduledDeparture(flightRequest.getScheduledDeparture());

        flight.setActualDeparture(flightRequest.getActualDeparture());
        flight.setActualArrival(flightRequest.getScheduledArrival());

        flight.setFlightStatus(flightRequest.getFlightStatus());

        if(flightRequest.getActive() != null) {
            flight.setActive(flightRequest.getActive());
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

                .createdAt(flight.getCreatedAt())
                .updatedAt(flight.getUpdatedAt())
                .build();
    }

}
