package com.fareservice.service.Impl;

import com.airlineportal.exception.ResourceNotFoundException;
import com.airlineportal.payload.request.Fare.FareCreateRequest;
import com.airlineportal.payload.request.Fare.FareQuoteRequest;
import com.airlineportal.payload.response.ApiResponse;
import com.airlineportal.payload.response.Fare.FareResponse;
import com.airlineportal.payload.response.Flight.FlightInstanceResponse;
import com.fareservice.external.ExternalService;
import com.fareservice.helper.FareHelper;
import com.fareservice.mapper.FareMapper;
import com.fareservice.model.Fare;
import com.fareservice.repository.FareRepository;
import com.fareservice.service.FareService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FareServiceImpl implements FareService {
    private final FareRepository fareRepository;
    private final FareHelper fareHelper;
    private final ExternalService externalService;

    @Override
    @Transactional
    public FareResponse createFare(FareCreateRequest request) {

        // Get Flight Instance
        ApiResponse<FlightInstanceResponse> instanceResponse = externalService.getInstanceById(request.getFlightInstanceId());
        if (instanceResponse == null || instanceResponse.getData() == null) {
            throw new ResourceNotFoundException("Flight instance not found with id: " + request.getFlightInstanceId());
        }

        FlightInstanceResponse instance = instanceResponse.getData();
        if (!instance.getFlightId().equals(request.getFlightId())) {
            throw new IllegalArgumentException("Flight ID does not match the flight instance");
        }


        fareHelper.validateDuplicate(request.getFlightInstanceId(), request.getFareClass());

        Fare fare = FareMapper.toEntity(request);
        Fare savedFare = fareRepository.save(fare);
        return FareMapper.toResponse(savedFare);
    }

    @Override
    public FareResponse getById(Long fareId) {
        Fare fare = fareHelper.findById(fareId);
        return FareMapper.toResponse(fare);
    }

    @Override
    public List<FareResponse> getByFlightInstance(Long flightInstanceId) {
        return fareRepository.findByFlightInstanceIdAndActiveTrue(flightInstanceId).stream()
                .map(FareMapper::toResponse)
                .toList();
    }

    @Override
    public FareResponse getFareQuote(FareQuoteRequest request) {
        Fare fare = fareHelper.findByFlightInstanceAndClass(request.getFlightInstanceId(), request.getFareClass());

        // Calculate for all passengers
        BigDecimal passengerCount = BigDecimal.valueOf(request.getPassengerCount());
        BigDecimal baseFare = fare.getBaseFare().multiply(passengerCount);
        BigDecimal tax = fare.getTax().multiply(passengerCount);
        BigDecimal totalFare = baseFare.add(tax);

        return FareMapper.toQuoteResponse(fare, baseFare, tax, totalFare);
    }



}
