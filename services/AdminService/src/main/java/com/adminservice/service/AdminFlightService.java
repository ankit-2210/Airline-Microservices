package com.adminservice.service;

import com.airlineportal.payload.response.ApiResponse;
import com.airlineportal.payload.response.Flight.FlightInstanceResponse;
import com.airlineportal.payload.response.Flight.FlightResponse;
import com.airlineportal.payload.response.Flight.FlightScheduleResponse;

public interface AdminFlightService {

    ApiResponse<FlightResponse> getFlightById(Long flightId);

    ApiResponse<FlightScheduleResponse> getScheduleById(Long scheduleId);

    ApiResponse<FlightInstanceResponse> getInstanceById(Long instanceId);



}
