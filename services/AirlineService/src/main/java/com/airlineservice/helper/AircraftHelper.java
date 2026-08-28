package com.airlineservice.helper;

import com.airlineportal.exception.ResourceAlreadyExistsException;
import com.airlineportal.exception.ResourceNotFoundException;
import com.airlineportal.payload.request.Airlines.Aircraft.AircraftRequest;
import com.airlineportal.utils.Airline.AircraftStatus;
import com.airlineservice.model.Aircraft;
import com.airlineservice.model.Airline;
import com.airlineservice.repository.AircraftRepository;
import com.airlineservice.repository.AirlineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AircraftHelper {
    private final AircraftRepository aircraftRepository;
    private final AirlineRepository airlineRepository;

    public Aircraft findById(Long aircraftId){
        if(aircraftId == null){
            throw new IllegalArgumentException("Aircraft id cannot be null");
        }

        return aircraftRepository.findById(aircraftId)
                .orElseThrow(() -> new ResourceNotFoundException("Aircraft not found with id: " + aircraftId));
    }

    public Airline findAirlineByOwner(Long ownerId){
        if(ownerId == null){
            throw new IllegalArgumentException("Owner id cannot be null");
        }

        return airlineRepository.findByOwnerId(ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("Airline not found for owner: " + ownerId));
    }



    public void validateCreate(AircraftRequest request, Long ownerId){
        if(request == null){
            throw new IllegalArgumentException("Aircraft request cannot be null");
        }

        if(ownerId == null){
            throw new IllegalArgumentException("Owner id cannot be null");
        }

        findAirlineByOwner(ownerId);

        validateCodeForCreate(request.getCode());
        validateSeatConfiguration(request);
    }

    public void validateUpdate(Aircraft aircraft, AircraftRequest request, Long ownerId){
        if(aircraft == null){
            throw new ResourceNotFoundException("Aircraft not found");
        }

        if(request == null){
            throw new IllegalArgumentException("Aircraft request cannot be null");
        }

        if(ownerId == null){
            throw new IllegalArgumentException("Owner id cannot be null");
        }

        validateOwnership(aircraft, ownerId);
        validateCodeForUpdate(request.getCode(), aircraft.getId());
        validateSeatConfiguration(request);
    }


    private void validateCodeForCreate(String code){
        String normalizedCode = normalizeCode(code);
        validateCodeFormat(normalizedCode);

        if(aircraftRepository.existsByCode(normalizedCode)) {
            throw new ResourceAlreadyExistsException("Aircraft code already exists: " + normalizedCode);
        }
    }

    private void validateCodeForUpdate(String code, Long aircraftId){
        String normalizedCode = normalizeCode(code);
        validateCodeFormat(normalizedCode);

        if(aircraftRepository.existsByCodeAndIdNot(normalizedCode, aircraftId)){
            throw new ResourceAlreadyExistsException("Aircraft code already exists: " + normalizedCode);
        }
    }

    private void validateCodeFormat(String code){
        if(code == null || code.isBlank()){
            throw new IllegalArgumentException("Aircraft code cannot be blank");
        }

        if(code.length() > 30){
            throw new IllegalArgumentException("Aircraft code cannot exceed 30 characters");
        }
    }

    public void validateSeatConfiguration(AircraftRequest request){
        Integer seatingCapacity = request.getSeatingCapacity();

        if(seatingCapacity == null){
            throw new IllegalArgumentException("Seating capacity cannot be null");
        }
        if(seatingCapacity < 1){
            throw new IllegalArgumentException("Seating capacity must be greater than zero");
        }

        int economySeats = safeValue(request.getEconomySeats());
        int premiumEconomySeats = safeValue(request.getPremiumEconomySeats());
        int businessSeats = safeValue(request.getBusinessSeats());
        int firstClassSeats = safeValue(request.getFirstClassSeats());

        int totalSeats = economySeats + premiumEconomySeats + businessSeats + firstClassSeats;
        if(totalSeats != seatingCapacity){
            throw new IllegalArgumentException("Seat configuration does not match " + "seating capacity. " + "Expected: " + seatingCapacity + ", Actual: " + totalSeats);
        }
    }


    public void validateOwnership(Aircraft aircraft, Long ownerId){
        if(aircraft == null){
            throw new ResourceNotFoundException("Aircraft not found");
        }

        if(ownerId == null){
            throw new IllegalArgumentException("Owner id cannot be null");
        }

        Airline airline = aircraft.getAirline();
        if(airline == null || airline.getOwnerId() == null || !airline.getOwnerId().equals(ownerId)){
            throw new ResourceNotFoundException("You are not authorized to access " + "this aircraft");
        }
    }

    public void normalizeEntity(Aircraft aircraft){
        if(aircraft == null)
            return;

        aircraft.setCode(normalizeCode(aircraft.getCode()));
        aircraft.setModel(normalizeModel(aircraft.getModel()));
        aircraft.setManufacturer(normalizeManufacturer(aircraft.getManufacturer()));

        if(aircraft.getAircraftStatus() == null){
            aircraft.setAircraftStatus(AircraftStatus.ACTIVE);
        }
        if(aircraft.getIsAvailable() == null){
            aircraft.setIsAvailable(true);
        }
        if(aircraft.getEconomySeats() == null){
            aircraft.setEconomySeats(0);
        }
        if(aircraft.getPremiumEconomySeats() == null){
            aircraft.setPremiumEconomySeats(0);
        }
        if(aircraft.getBusinessSeats() == null){
            aircraft.setBusinessSeats(0);
        }
        if(aircraft.getFirstClassSeats() == null){
            aircraft.setFirstClassSeats(0);
        }
    }


    public void validateCanDelete(Aircraft aircraft){
        if(aircraft == null){
            throw new ResourceNotFoundException("Aircraft not found");
        }

        /*
         * Additional deletion rules can be added here later.
         *
         * For example:
         *
         * - Cannot delete aircraft assigned to a flight
         * - Cannot delete aircraft currently in operation
         * - Cannot delete aircraft under maintenance
         */
    }


    // Normalization Methods
    public String normalizeCode(String code){
        if(code == null)
            return null;
        return code.trim().toUpperCase();
    }

    public String normalizeModel(String model){
        if(model == null)
            return null;
        return model.trim();
    }

    public String normalizeManufacturer(String manufacturer){
        if(manufacturer == null)
            return null;
        return manufacturer.trim();
    }

    private int safeValue(Integer value){
        return value == null ? 0 : value;
    }


}
