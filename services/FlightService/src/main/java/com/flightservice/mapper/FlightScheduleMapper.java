package com.flightservice.mapper;

import com.flightservice.model.FlightSchedule;
import com.microservices.payload.request.Flight.FlightScheduleRequest;
import com.microservices.payload.response.Flight.FlightScheduleResponse;

public class FlightScheduleMapper {
    public static FlightSchedule toEntity(FlightScheduleRequest request){
        return FlightSchedule.builder()
                .departureTime(request.getDepartureTime())
                .arrivalTime(request.getArrivalTime())

                .startDate(request.getStartDate())
                .endDate(request.getEndDate())

                .operatingDays(request.getOperatingDays())
                .active(request.getActive() != null ? request.getActive() : true)
                .build();
    }

    public static void updateEntity(FlightSchedule schedule, FlightScheduleRequest request){
        schedule.setDepartureTime(request.getDepartureTime());
        schedule.setArrivalTime(request.getArrivalTime());

        schedule.setStartDate(request.getStartDate());
        schedule.setEndDate(request.getEndDate());

        schedule.setOperatingDays(request.getOperatingDays());
        if(request.getActive() != null){
            schedule.setActive(request.getActive());
        }
    }

    public static FlightScheduleResponse toResponse(FlightSchedule schedule){
        return FlightScheduleResponse.builder()
                .id(schedule.getId())

                .flightId(schedule.getFlight().getId())
                .flightNumber(schedule.getFlight().getFlightNumber())

                .departureAirportId(schedule.getFlight().getDepartureAirportId())
                .arrivalAirportId(schedule.getFlight().getArrivalAirportId())

                .departureTime(schedule.getDepartureTime())
                .arrivalTime(schedule.getArrivalTime())

                .startDate(schedule.getStartDate())
                .endDate(schedule.getEndDate())

                .operatingDays(schedule.getOperatingDays())
                .active(schedule.getActive())

                .createdAt(schedule.getCreatedAt())
                .updatedAt(schedule.getUpdatedAt())
                .build();
    }

}
