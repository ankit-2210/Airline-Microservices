package com.flightservice.elasticsearch.mapper;

import com.airlineportal.payload.response.Airlines.Aircraft.AircraftResponse;
import com.airlineportal.payload.response.Airlines.Airline.AirlineResponse;
import com.airlineportal.payload.response.Location.Airport.AirportResponse;
import com.flightservice.model.Flight;
import com.flightservice.model.FlightInstance;
import com.flightservice.elasticsearch.document.FlightInstanceDocument;

public class FlightInstanceSearchMapper {

    public static FlightInstanceDocument toDocument(FlightInstance instance,
            AirlineResponse airline, AircraftResponse aircraft,
            AirportResponse departureAirport, AirportResponse arrivalAirport){

        Flight flight = instance.getFlight();
        return FlightInstanceDocument.builder()
                .id(instance.getId())

                .flightId(flight.getId())
                .flightNumber(flight.getFlightNumber())

                .airlineId(flight.getAirlineId())
                .airlineName(airline.getName())
                .airlineLogo(airline.getLogoUrl())

                .aircraftId(flight.getAircraftId())
                .aircraftModel(aircraft.getModel())
                .aircraftCode(aircraft.getCode())

                .departureAirportId(flight.getDepartureAirportId())
                .departureAirportCode(departureAirport.getIataCode())
                .departureAirportName(departureAirport.getName())

                .arrivalAirportId(flight.getArrivalAirportId())
                .arrivalAirportCode(arrivalAirport.getIataCode())
                .arrivalAirportName(arrivalAirport.getName())

                .departureDateTime(instance.getDepartureDateTime())
                .arrivalDateTime(instance.getArrivalDateTime())

                .totalSeats(instance.getTotalSeats())
                .availableSeats(instance.getAvailableSeats())
                .bookedSeats(instance.getBookedSeats())

                .flightStatus(instance.getFlightStatus())

                .minAdvanceBookingDays(instance.getMinAdvanceBookingDays())
                .maxAdvanceBookingDays(instance.getMaxAdvanceBookingDays())

                .active(instance.getActive())

                .build();

    }

}
