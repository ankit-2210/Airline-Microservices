package com.locationservice.helper;

import com.airlineportal.exception.ResourceNotFoundException;
import com.airlineportal.payload.request.Location.Airport.AirportRequest;
import com.locationservice.model.Airport;
import com.locationservice.model.City;
import com.locationservice.repository.AirportRepository;
import com.locationservice.repository.CityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AirportHelper {
    private final AirportRepository airportRepository;
    private final CityRepository cityRepository;

    public void normalizeRequest(AirportRequest request) {
        if(request == null)
            return;

        if(request.getIataCode() != null){
            request.setIataCode(request.getIataCode().trim().toUpperCase());
        }
        if(request.getName() != null){
            request.setName(request.getName().trim());
        }
        if(request.getTimeZoneId() != null){
            request.setTimeZoneId(request.getTimeZoneId().trim());
        }
    }

    public void validateIataCodeForCreate(String iataCode){
        String normalizedCode = normalizeIataCode(iataCode);
        if(normalizedCode == null || normalizedCode.isBlank())
            return;

        if(airportRepository.existsByIataCode(normalizedCode)){
            throw new IllegalArgumentException("Airport with IATA code '" + normalizedCode + "' already exists");
        }
    }

    public void validateIataCodeForUpdate(String iataCode, Long airportId){
        String normalizedCode = normalizeIataCode(iataCode);
        if(normalizedCode == null || normalizedCode.isBlank())
            return;

        if(airportRepository.existsByIataCodeAndIdNot(normalizedCode, airportId)){
            throw new IllegalArgumentException("Airport with IATA code '" + normalizedCode + "' already exists");
        }
    }

    public Airport findAirportByIataCode(String iataCode) {
        String normalizedCode = normalizeIataCode(iataCode);
        return airportRepository.findByIataCodeIgnoreCase(normalizedCode)
                .orElseThrow(() -> new ResourceNotFoundException("Airport not found with IATA code: " + normalizedCode));
    }


    public Airport findAirportById(Long id){
        return airportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Airport not found with id: " + id));
    }

    public City findCityById(Long cityId) {
        return cityRepository.findById(cityId)
                .orElseThrow(() -> new ResourceNotFoundException("City not found with id: " + cityId));
    }

    public String normalizeIataCode(String iataCode) {
        if(iataCode == null)
            return null;

        return iataCode.trim().toUpperCase();
    }


}
