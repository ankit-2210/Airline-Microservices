package com.flightservice.mapper;

import com.flightservice.model.Flight;
import com.flightservice.model.FlightInstance;
import com.flightservice.model.FlightSchedule;
import com.microservices.payload.request.Flight.FlightInstanceRequest;
import com.microservices.payload.response.Airlines.Aircraft.AircraftResponse;
import com.microservices.payload.response.Airlines.Airline.AirlineResponse;
import com.microservices.payload.response.Location.Airport.AirportResponse;
import com.microservices.payload.response.Flight.FlightInstanceResponse;

public class FlightInstanceMapper {
    public static FlightInstance toEntity(FlightInstanceRequest flightInstanceRequest, Flight flight, FlightSchedule schedule){
        return FlightInstance.builder()
                .flight(flight)
                .schedule(schedule)

                .departureDateTime(flightInstanceRequest.getDepartureDateTime())
                .arrivalDateTime(flightInstanceRequest.getArrivalDateTime())

                .totalSeats(flightInstanceRequest.getTotalSeats())
                .availableSeats(flightInstanceRequest.getAvailableSeats() != null ? flightInstanceRequest.getAvailableSeats() : flightInstanceRequest.getTotalSeats())

                .flightStatus(flightInstanceRequest.getFlightStatus())

                .minAdvanceBookingDays(flightInstanceRequest.getMinAdvanceBookingDays())
                .maxAdvanceBookingDays(flightInstanceRequest.getMaxAdvanceBookingDays())

                .active(flightInstanceRequest.getActive() != null ? flightInstanceRequest.getActive() : true)
                .build();
    }

    public static void updateEntity(FlightInstance flightInstance, FlightInstanceRequest flightInstanceRequest){
        flightInstance.setDepartureDateTime(flightInstanceRequest.getDepartureDateTime());
        flightInstance.setArrivalDateTime(flightInstanceRequest.getArrivalDateTime());

        flightInstance.setTotalSeats(flightInstanceRequest.getTotalSeats());
        if(flightInstanceRequest.getAvailableSeats() != null){
            flightInstance.setAvailableSeats(flightInstanceRequest.getAvailableSeats());
        }

        if(flightInstanceRequest.getFlightStatus() != null){
            flightInstance.setFlightStatus(flightInstanceRequest.getFlightStatus());
        }

        flightInstance.setMinAdvanceBookingDays(flightInstanceRequest.getMinAdvanceBookingDays());
        flightInstance.setMaxAdvanceBookingDays(flightInstanceRequest.getMaxAdvanceBookingDays());

        if(flightInstanceRequest.getActive() != null){
            flightInstanceRequest.setActive(flightInstanceRequest.getActive());
        }
    }

    public static FlightInstanceResponse toResponse(FlightInstance flightInstance, AircraftResponse aircraftResponse,
                                                    AirlineResponse airlineResponse,
                                                    AirportResponse departureAirport, AirportResponse arrivalAirport){
        Flight flight = flightInstance.getFlight();
        return FlightInstanceResponse.builder()
                .id(flightInstance.getId())

                .flightId(flight.getId())
                .flightNumber(flight.getFlightNumber())

                .scheduleId(flightInstance.getSchedule().getId())

                .airlineId(flight.getAirlineId())
                .airlineName(airlineResponse.getName())
                .airlineLogo(airlineResponse.getLogoUrl())

                .aircraftId(flight.getAircraftId())
                .aircraftModel(aircraftResponse.getModel())
                .aircraftCode(aircraftResponse.getCode())

                .departureAirport(departureAirport)
                .arrivalAirport(arrivalAirport)

                .departureDateTime(flightInstance.getDepartureDateTime())
                .arrivalDateTime(flightInstance.getArrivalDateTime())

                .formattedDuration(flightInstance.getFormatedDuration())

                .totalSeats(flightInstance.getTotalSeats())
                .availableSeats(flightInstance.getAvailableSeats())
                .bookedSeats(flightInstance.getBookedSeats())

                .soldOut(flightInstance.isSoldOut())
                .bookingOpen(flightInstance.isBookingOpen())
                .canCheckIn(flightInstance.canCheckIn())

                .flightStatus(flightInstance.getFlightStatus())
                .minAdvanceBookingDays(flightInstance.getMinAdvanceBookingDays())
                .maxAdvanceBookingDays(flightInstance.getMaxAdvanceBookingDays())

                .active(flightInstance.getActive())

                .createdAt(flightInstance.getCreatedAt())
                .updatedAt(flightInstance.getUpdatedAt())
                .build();


    }

}
