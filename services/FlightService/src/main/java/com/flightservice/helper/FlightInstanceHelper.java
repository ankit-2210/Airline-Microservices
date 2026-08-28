package com.flightservice.helper;


import com.airlineportal.exception.ResourceAlreadyExistsException;
import com.airlineportal.exception.ResourceNotFoundException;
import com.airlineportal.payload.request.Flight.FlightInstanceRequest;
import com.flightservice.model.Flight;
import com.flightservice.model.FlightInstance;
import com.flightservice.model.FlightSchedule;
import com.flightservice.repository.FlightInstanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class FlightInstanceHelper {
    private final FlightInstanceRepository flightInstanceRepository;

    public FlightInstance findById(Long instanceId){
        if(instanceId == null){
            throw new IllegalArgumentException("Flight instance id cannot be null");
        }

        return flightInstanceRepository.findById(instanceId)
                .orElseThrow(() -> new ResourceNotFoundException("Flight instance not found with id: " + instanceId));
    }

    public FlightInstance findByIdAndAirline(Long instanceId, Long airlineId){
        if(instanceId == null){
            throw new IllegalArgumentException("Flight instance id cannot be null");
        }
        if(airlineId == null){
            throw new IllegalArgumentException("Airline id cannot be null");
        }

        return flightInstanceRepository.findByIdAndFlightAirlineId(instanceId, airlineId)
                .orElseThrow(() -> new ResourceNotFoundException("Flight instance not found or you are not authorized"));
    }


    public void validateCreate(FlightInstanceRequest request, Flight flight, FlightSchedule schedule){

        validateRequest(request);
        validateFlight(flight);
        validateSchedule(schedule);
        validateScheduleBelongsToFlight(schedule, flight);
        validateDateTime(request);
        validateOperatingDate(request, schedule);
        validateSeats(request);
        validateBookingRules(request);

        validateDuplicateInstance(schedule.getId(), request.getDepartureDateTime());

    }

    public void validateUpdate(FlightInstance instance, FlightInstanceRequest request){
        if(instance == null){
            throw new ResourceNotFoundException("Flight instance not found");
        }

        validateRequest(request);

        validateFlight(instance.getFlight());
        validateSchedule(instance.getSchedule());

        validateDateTime(request);
        validateOperatingDate(request, instance.getSchedule());

        validateSeats(request);
        validateBookingRules(request);
    }

    private void validateRequest(FlightInstanceRequest request){
        if(request == null){
            throw new IllegalArgumentException("Flight instance request cannot be null");
        }
    }

    private void validateFlight(Flight flight){
        if(flight == null){
            throw new ResourceNotFoundException("Flight not found");
        }
        if(flight.getId() == null){
            throw new IllegalArgumentException("Flight id cannot be null");
        }
        if(flight.getAirlineId() == null){
            throw new IllegalArgumentException("Flight airline id cannot be null");
        }
    }

    private void validateSchedule(FlightSchedule schedule){
        if(schedule == null){
            throw new ResourceNotFoundException("Flight schedule not found");
        }
        if(schedule.getId() == null){
            throw new IllegalArgumentException("Schedule id cannot be null");
        }
        if(!Boolean.TRUE.equals(schedule.getActive())){
            throw new IllegalArgumentException("Cannot create instance from inactive schedule");
        }
    }

    private void validateScheduleBelongsToFlight(FlightSchedule schedule, Flight flight){
        if(schedule.getFlight() == null){
            throw new ResourceNotFoundException("Schedule flight not found");
        }
        if(schedule.getFlight().getId() == null){
            throw new IllegalArgumentException("Schedule flight id cannot be null");
        }
        if(!schedule.getFlight().getId().equals(flight.getId())){
            throw new IllegalArgumentException("Schedule does not belong to this flight");
        }
    }

    private void validateDateTime(FlightInstanceRequest request){
        LocalDateTime departure = request.getDepartureDateTime();
        LocalDateTime arrival = request.getArrivalDateTime();

        if(departure == null){
            throw new IllegalArgumentException("Departure date time cannot be null");
        }
        if(arrival == null){
            throw new IllegalArgumentException("Arrival date time cannot be null");
        }
        if(!arrival.isAfter(departure)){
            throw new IllegalArgumentException("Arrival date time must be after departure date time");
        }
    }


    private void validateOperatingDate(FlightInstanceRequest request, FlightSchedule schedule){
        LocalDateTime departure = request.getDepartureDateTime();
        if(departure == null)
            return;

        if(!schedule.isOperatingOn(departure.toLocalDate())){
            throw new IllegalArgumentException("Flight schedule does not operate on " + departure.toLocalDate());
        }
    }


    private void validateSeats(FlightInstanceRequest request){
        Integer totalSeats = request.getTotalSeats();
        Integer availableSeats = request.getAvailableSeats() == null ? totalSeats : request.getAvailableSeats();

        if(totalSeats == null || totalSeats < 1){
            throw new IllegalArgumentException("Total seats must be greater than zero");
        }
        if(availableSeats < 0){
            throw new IllegalArgumentException("Available seats cannot be negative");
        }
        if(availableSeats > totalSeats){
            throw new IllegalArgumentException("Available seats cannot exceed total seats");
        }
    }

    private void validateBookingRules(FlightInstanceRequest request){
        Integer minDays = request.getMinAdvanceBookingDays();
        Integer maxDays = request.getMaxAdvanceBookingDays();

        if(minDays != null && minDays < 0){
            throw new IllegalArgumentException("Minimum advance booking days cannot be negative");
        }
        if(maxDays != null && maxDays < 0){
            throw new IllegalArgumentException("Maximum advance booking days cannot be negative");
        }
        if(minDays != null && maxDays != null && maxDays < minDays){
            throw new IllegalArgumentException("Maximum advance booking days cannot be less than minimum advance booking days");
        }
    }

    private void validateDuplicateInstance(Long scheduleId, LocalDateTime departureDateTime){
        if(flightInstanceRepository.existsByScheduleIdAndDepartureDateTime(scheduleId, departureDateTime)){
            throw new ResourceAlreadyExistsException("Flight instance already exists for this schedule and departure time");
        }
    }

    public void validateCanDelete(FlightInstance instance){
        if(instance == null){
            throw new ResourceNotFoundException("Flight instance not found");
        }
        if(instance.getDepartureDateTime() != null && !LocalDateTime.now().isBefore(instance.getDepartureDateTime())){
            throw new IllegalArgumentException("Cannot delete a flight instance that has already started");
        }

        /*
         * Later add booking-related restrictions:
         *
         * - Cannot delete if bookings exist.
         * - Cannot delete if payment exists.
         * - Cannot delete if check-in has started.
         */
    }


}
