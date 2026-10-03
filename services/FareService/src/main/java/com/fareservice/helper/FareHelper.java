package com.fareservice.helper;

import com.airlineportal.utils.Fare.FareClass;
import com.fareservice.model.Fare;
import com.fareservice.repository.FareRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FareHelper {
    private final FareRepository fareRepository;

    public Fare findById(Long fareId) {
        return fareRepository.findById(fareId)
                .orElseThrow(() -> new RuntimeException("Fare not found with ID: " + fareId));
    }

    public Fare findByFlightInstanceAndClass(Long flightInstanceId, FareClass fareClass){
        return fareRepository.findByFlightInstanceIdAndFareClassAndActiveTrue(flightInstanceId, fareClass)
                .orElseThrow(() -> new RuntimeException("Active fare not found for flight instance " + flightInstanceId + " and fare class " + fareClass));
    }

    public void validateDuplicate(Long flightInstanceId, FareClass fareClass){
        if(fareRepository.existsByFlightInstanceIdAndFareClass(flightInstanceId, fareClass)){
            throw new RuntimeException("Fare already exists for flight instance " + flightInstanceId + " and fare class " + fareClass);
        }

    }

}
