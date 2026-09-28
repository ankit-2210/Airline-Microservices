package com.flightservice.helper;


import com.airlineportal.exception.ResourceAlreadyExistsException;
import com.airlineportal.exception.ResourceNotFoundException;
import com.airlineportal.payload.request.Flight.FlightRequest;
import com.airlineportal.utils.Flight.FlightStatus;
import com.flightservice.external.ExternalService;
import com.flightservice.model.Flight;
import com.flightservice.repository.FlightRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class FlightHelper {
    private final FlightRepository flightRepository;
    private final ExternalService externalService;

    public Flight findById(Long flightId) {
        if(flightId == null){
            throw new IllegalArgumentException("Flight id cannot be null");
        }
        return flightRepository.findById(flightId)
                .orElseThrow(() -> new ResourceNotFoundException("Flight not found with id: " + flightId));
    }

    public Flight findByAirlineAndId(Long airlineId, Long flightId){
        if(airlineId == null){
            throw new IllegalArgumentException("Airline id cannot be null");
        }
        if(flightId == null){
            throw new IllegalArgumentException("Flight id cannot be null");
        }

        return flightRepository.findByAirlineIdAndId(airlineId, flightId)
                .orElseThrow(() -> new ResourceNotFoundException("Flight not found with id: " + flightId + " for airline: " + airlineId));
    }

    public String normalizeFlightNumber(String flightNumber){
        if(flightNumber == null)
            return null;
        return flightNumber.trim().toUpperCase();
    }

    // Normalize Entity
    public void normalizeEntity(Flight flight){
        if(flight == null)
            return;

        flight.setFlightNumber(normalizeFlightNumber(flight.getFlightNumber()));
    }


    // Create Validation
    public void validateCreate(FlightRequest request){
        if(request == null){
            throw new IllegalArgumentException("Flight request cannot be null");
        }

        validateFlightNumberForCreate(request.getFlightNumber());

        validateAirlineId(request.getAirlineId());
        validateAircraftId(request.getAircraftId());

        validateAircraftBelongsToAirline(request.getAircraftId(), request.getAirlineId());

        validateAirportIds(request.getDepartureAirportId(), request.getArrivalAirportId());
        validateSchedule(request.getScheduledDeparture(), request.getScheduledArrival());

        validateStatus(request.getFlightStatus());

    }

    public void validateUpdate(Flight flight, FlightRequest request){
        if(flight == null){
            throw new ResourceNotFoundException("Flight not found");
        }
        if(request == null){
            throw new IllegalArgumentException("Flight request cannot be null");
        }

        validateFlightNumberForUpdate(request.getFlightNumber(), flight.getId());

        validateAirlineId(request.getAirlineId());
        validateAircraftId(request.getAircraftId());

        validateAircraftBelongsToAirline(request.getAircraftId(), request.getAirlineId());

        validateAirportIds(request.getDepartureAirportId(), request.getArrivalAirportId());
        validateSchedule(request.getScheduledDeparture(), request.getScheduledArrival());

        validateStatus(request.getFlightStatus());

    }

    // Flight Number
    private void validateFlightNumberForCreate(String flightNumber){
        String normalizedFlightNumber = normalizeFlightNumber(flightNumber);
        if (normalizedFlightNumber == null || normalizedFlightNumber.isBlank()){
            throw new IllegalArgumentException("Flight number cannot be blank");
        }

        if(flightRepository.existsByFlightNumber(normalizedFlightNumber)){
            throw new ResourceAlreadyExistsException("Flight number already exists: " + normalizedFlightNumber);
        }

    }

    private void validateFlightNumberForUpdate(String flightNumber, Long flightId){
        String normalizedFlightNumber = normalizeFlightNumber(flightNumber);
        if(normalizedFlightNumber == null || normalizedFlightNumber.isBlank()){
            throw new IllegalArgumentException("Flight number cannot be blank");
        }

        if(flightRepository.existsByFlightNumberAndIdNot(normalizedFlightNumber, flightId)){
            throw new ResourceAlreadyExistsException("Flight number already exists: " + normalizedFlightNumber);
        }

    }


    // Airline / Aircraft
    private void validateAirlineId(Long airlineId) {
        if(airlineId == null){
            throw new IllegalArgumentException("Airline id cannot be null");
        }

        externalService.getAirlineById(airlineId);
    }

    private void validateAircraftId(Long aircraftId) {
        if(aircraftId == null){
            throw new IllegalArgumentException("Aircraft id cannot be null");
        }

        externalService.getAircraftById(aircraftId);
    }

    private void validateAircraftBelongsToAirline(Long aircraftId, Long airlineId){
        if (!externalService.aircraftBelongsToAirline(aircraftId, airlineId).getData()){
            throw new ResourceNotFoundException("Aircraft does not belong to the specified airline");
        }
    }


    private void validateAirportIds(Long departureAirportId, Long arrivalAirportId){
        if(departureAirportId == null){
            throw new IllegalArgumentException("Departure airport id cannot be null");
        }
        if(arrivalAirportId == null){
            throw new IllegalArgumentException("Arrival airport id cannot be null");
        }
        if(departureAirportId.equals(arrivalAirportId)){
            throw new IllegalArgumentException("Departure and arrival airports cannot be the same");
        }

        externalService.getAirportById(departureAirportId);
        externalService.getAirportById(arrivalAirportId);

    }



    // Schedule
    private void validateSchedule(LocalDateTime scheduledDeparture, LocalDateTime scheduledArrival){
        if(scheduledDeparture == null){
            throw new IllegalArgumentException("Scheduled departure cannot be null");
        }
        if(scheduledArrival == null){
            throw new IllegalArgumentException("Scheduled arrival cannot be null");
        }
        if(!scheduledArrival.isAfter(scheduledDeparture)){
            throw new IllegalArgumentException("Scheduled arrival must be after scheduled departure");
        }
    }


    private void validateStatus(FlightStatus status){
        if (status == null) {
            return;
        }

        // Keep status business rules here.
        //
        // Examples for future:
        // SCHEDULED -> DEPARTED
        // DEPARTED -> ARRIVED
        // CANCELLED -> cannot be reactivated
    }



    // Delete
    public void validateCanDelete(Flight flight){
        if(flight == null) {
            throw new ResourceNotFoundException("Flight not found");
        }

        if(FlightStatus.DEPARTED.equals(flight.getFlightStatus()) || FlightStatus.ARRIVED.equals(flight.getFlightStatus())){
            throw new IllegalArgumentException("Departed or arrived flight cannot be deleted");
        }

        /*
         * Additional deletion rules can be added here.
         *
         * For example:
         *
         * - Cannot delete flight if it has FlightInstances
         * - Cannot delete flight after departure
         * - Cannot delete a flight with active bookings
         * - Cannot delete a departed/arrived flight
         */
    }



}
