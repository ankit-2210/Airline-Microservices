package com.flightservice.helper;


import com.airlineportal.exception.ResourceNotFoundException;
import com.airlineportal.payload.request.Flight.FlightScheduleRequest;
import com.flightservice.model.Flight;
import com.flightservice.model.FlightSchedule;
import com.flightservice.repository.FlightScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class FlightScheduleHelper {
    private final FlightScheduleRepository flightScheduleRepository;

    public FlightSchedule findById(Long scheduleId){
        if(scheduleId == null){
            throw new IllegalArgumentException("Schedule id cannot be null");
        }

        return flightScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ResourceNotFoundException("Flight schedule not found with id: " + scheduleId));
    }

    public FlightSchedule findByIdAndAirline(Long scheduleId, Long airlineId){
        if (scheduleId == null){
            throw new IllegalArgumentException("Schedule id cannot be null");
        }

        if (airlineId == null){
            throw new IllegalArgumentException("Airline id cannot be null");
        }

        return flightScheduleRepository.findByIdAndFlightAirlineId(scheduleId, airlineId)
                .orElseThrow(() -> new ResourceNotFoundException("Flight schedule not found or you are not authorized"));
    }

    public void validateCreate(FlightScheduleRequest request, Flight flight){
        validateRequest(request);
        validateFlight(flight);
        validateSchedule(request);

    }

    public void validateUpdate(FlightSchedule schedule, FlightScheduleRequest request, Long airlineId){
        if(schedule == null){
            throw new ResourceNotFoundException("Flight schedule not found");
        }
        if(airlineId == null){
            throw new IllegalArgumentException("Airline id cannot be null");
        }

        validateRequest(request);
        validateFlight(schedule.getFlight());
        validateOwnership(schedule, airlineId);
        validateSchedule(request);
    }


    private void validateRequest(FlightScheduleRequest request){
        if(request == null){
            throw new IllegalArgumentException("Flight schedule request cannot be null");
        }
    }

    private void validateFlight(Flight flight){
        if (flight == null) {
            throw new ResourceNotFoundException("Flight not found");
        }
        if (flight.getId() == null) {
            throw new IllegalArgumentException("Flight id cannot be null");
        }
    }

    private void validateOwnership(FlightSchedule schedule, Long airlineId){
        Flight flight = schedule.getFlight();
        if(flight == null || flight.getAirlineId() == null || !flight.getAirlineId().equals(airlineId)){
            throw new ResourceNotFoundException("You are not authorized to access this flight schedule");
        }
    }

    private void validateSchedule(FlightScheduleRequest request){
        validateTime(request.getDepartureTime(), request.getArrivalTime());
        validateDateRange(request.getStartDate(), request.getEndDate());
        validateOperatingDays(request.getOperatingDays());

    }


    private void validateTime(LocalTime departureTime, LocalTime arrivalTime){
        if(departureTime == null){
            throw new IllegalArgumentException("Departure time cannot be null");
        }
        if(arrivalTime == null){
            throw new IllegalArgumentException("Arrival time cannot be null");
        }
    }

    private void validateDateRange(LocalDate startDate, LocalDate endDate){
        if(startDate == null){
            throw new IllegalArgumentException("Start date cannot be null");
        }
        if(endDate == null){
            throw new IllegalArgumentException("End date cannot be null");
        }
        if(endDate.isBefore(startDate)){
            throw new IllegalArgumentException("End date cannot be before start date");
        }
    }

    private void validateOperatingDays(Set<DayOfWeek> operatingDays){
        if(operatingDays == null || operatingDays.isEmpty()){
            throw new IllegalArgumentException("At least one operating day is required");
        }
    }

    public void validateCanDelete(FlightSchedule schedule){
        if(schedule == null){
            throw new ResourceNotFoundException("Flight schedule not found");
        }

        /*
         * Add deletion rules here later.
         *
         * Example:
         * - Cannot delete if future flight instances exist.
         * - Cannot delete if bookings exist.
         */
    }






}
