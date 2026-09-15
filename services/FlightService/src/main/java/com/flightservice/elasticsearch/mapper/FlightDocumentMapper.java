package com.flightservice.elasticsearch.mapper;

import com.flightservice.elasticsearch.document.FlightDocument;
import com.flightservice.model.Flight;

public final class FlightDocumentMapper {
    private FlightDocumentMapper(){

    }

    public static FlightDocument toDocument(Flight flight){
        if(flight == null)
            return null;

        return FlightDocument.builder()
                .id(flight.getId())
                .flightNumber(flight.getFlightNumber())

                .airlineId(flight.getAirlineId())
                .aircraftId(flight.getAircraftId())

                .departureAirportId(flight.getDepartureAirportId())
                .arrivalAirportId(flight.getArrivalAirportId())

                .scheduledDeparture(flight.getScheduledDeparture())
                .scheduledArrival(flight.getScheduledArrival())

                .actualDeparture(flight.getActualDeparture())
                .actualArrival(flight.getActualArrival())

                .flightStatus(flight.getFlightStatus())
                .active(flight.getActive())

                .delayed(flight.getDelayed())
                .delayMinutes(flight.getDelayMinutes())

                .searchableDeparture(flight.getScheduledDeparture())
                .searchableArrival(flight.getScheduledArrival())

                .build();

    }

}
