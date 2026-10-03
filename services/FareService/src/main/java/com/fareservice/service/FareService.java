package com.fareservice.service;

import com.airlineportal.payload.request.Fare.FareCreateRequest;
import com.airlineportal.payload.request.Fare.FareQuoteRequest;
import com.airlineportal.payload.response.Fare.FareResponse;

import java.util.*;

public interface FareService {
    FareResponse createFare(FareCreateRequest request);
    FareResponse getById(Long fareId);

    List<FareResponse> getByFlightInstance(Long flightInstanceId);

    FareResponse getFareQuote(FareQuoteRequest request);


}
