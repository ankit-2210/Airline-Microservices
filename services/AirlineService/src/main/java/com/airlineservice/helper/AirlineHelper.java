package com.airlineservice.helper;

import com.airlineportal.exception.ResourceAlreadyExistsException;
import com.airlineportal.exception.ResourceNotFoundException;
import com.airlineportal.payload.request.Airlines.Airline.AirlineRequest;
import com.airlineservice.model.Airline;
import com.airlineservice.repository.AirlineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AirlineHelper {
    private final AirlineRepository airlineRepository;

    public Airline findById(Long airlineId){
        if(airlineId == null)
            throw new IllegalArgumentException("Airline id cannot be null");

        return airlineRepository.findById(airlineId)
                .orElseThrow(() -> new ResourceNotFoundException("Airline not found with id: " + airlineId));
    }

    public Airline findByOwnerId(Long ownerId){
        if(ownerId == null)
            throw new IllegalArgumentException("Owner id cannot be null");

        return airlineRepository.findByOwnerId(ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("Airline not found with owner id: " + ownerId));
    }

    // Normalization
    public String normalizeIataCode(String iataCode){
        if(iataCode == null)
            return null;

        return iataCode.trim().toUpperCase();
    }


    public String normalizeIcaoCode(String icaoCode){
        if(icaoCode == null)
            return null;

        return icaoCode.trim().toUpperCase();
    }

    public String normalizeName(String name){
        if(name == null)
            return null;

        return name.trim();
    }

    public String normalizeAlias(String alias){
        if(alias == null)
            return null;

        return alias.trim();
    }

    public String normalizeCountry(String country){
        if(country == null)
            return null;

        return country.trim();
    }

    public String normalizeAlliance(String alliance){
        if(alliance == null)
            return null;

        return alliance.trim();
    }


    // Create Validation
    public void validateCreate(AirlineRequest request, Long ownerId){
        validateRequest(request);

        if(ownerId == null){
            throw new IllegalArgumentException("Owner id cannot be null");
        }

        validateOwnerDoesNotHaveAirline(ownerId);
        validateCodesForCreate(request);
    }


    // Update Validation
    public void validateUpdate(Airline airline, AirlineRequest request, Long ownerId){
        validateRequest(request);

        if(ownerId == null){
            throw new IllegalArgumentException("Owner id cannot be null");
        }

        validateOwnership(airline, ownerId);
        validateCodesForUpdate(request, airline.getId());
    }



    // Request Validation
    private void validateRequest(AirlineRequest request){
        if(request == null){
            throw new IllegalArgumentException("Airline request cannot be null");
        }
    }

    // Owner Validation
    private void validateOwnerDoesNotHaveAirline(Long ownerId){
        if(airlineRepository.findByOwnerId(ownerId).isPresent()){
            throw new ResourceAlreadyExistsException("Owner already has an airline");
        }
    }

    // Code Validation - Create
    private void validateCodesForCreate(AirlineRequest request){
        String iataCode = normalizeIataCode(request.getIataCode());
        String icaoCode = normalizeIcaoCode(request.getIcaoCode());

        validateIcaoCode(iataCode);
        validateIcaoCode(icaoCode);

        if(airlineRepository.existsByIataCode(iataCode)){
            throw new ResourceAlreadyExistsException("Airline with IATA code already exists: " + iataCode);
        }
        if(airlineRepository.existsByIcaoCode(icaoCode)){
            throw new ResourceAlreadyExistsException("Airline with ICAO code already exists: " + icaoCode);
        }
    }


    // Code Validation - Update
    private void validateCodesForUpdate(AirlineRequest request, Long airlineId){
        String iataCode = normalizeIataCode(request.getIataCode());
        String icaoCode = normalizeIcaoCode(request.getIcaoCode());

        validateIataCode(iataCode);
        validateIcaoCode(icaoCode);

        if(airlineRepository.existsByIataCodeAndIdNot(iataCode, airlineId)){
            throw new ResourceAlreadyExistsException("Airline with IATA code already exists: " + iataCode);
        }
        if(airlineRepository.existsByIcaoCodeAndIdNot(icaoCode, airlineId)){
            throw new ResourceAlreadyExistsException("Airline with ICAO code already exists: " + icaoCode);
        }
    }


    // ICAO Validation
    private void validateIataCode(String iataCode){
        if(iataCode == null || iataCode.isBlank()){
            throw new IllegalArgumentException("IATA code cannot be blank");
        }
        if(iataCode.length() != 2){
            throw new IllegalArgumentException("IATA code must contain exactly 2 characters");
        }
    }

    // ICAO Validation
    private void validateIcaoCode(String icaoCode){
        if(icaoCode == null || icaoCode.isBlank()){
            throw new IllegalArgumentException("ICAO code cannot be blank");
        }
        if(icaoCode.length() != 3){
            throw new IllegalArgumentException("ICAO code must contain exactly 3 characters");
        }
    }


    // Ownership
    public void validateOwnership(Airline airline, Long ownerId) {
        if(airline == null){
            throw new ResourceNotFoundException("Airline not found");
        }
        if(ownerId == null){
            throw new IllegalArgumentException("Owner id cannot be null");
        }

        if(airline.getOwnerId() == null || !airline.getOwnerId().equals(ownerId)) {
            throw new ResourceNotFoundException("You are not authorized to access this airline");
        }

    }


    // Normalize Entity
    public void normalizeEntity(Airline airline){
        if(airline == null)
            return;

        airline.setIataCode(normalizeIataCode(airline.getIataCode()));
        airline.setIcaoCode(normalizeIcaoCode(airline.getIcaoCode()));
        airline.setName(normalizeName(airline.getName()));
        airline.setAlias(normalizeAlias(airline.getAlias()));
        airline.setCountry(normalizeCountry(airline.getCountry()));
        airline.setAlliance(normalizeAlliance(airline.getAlliance()));

    }


}
