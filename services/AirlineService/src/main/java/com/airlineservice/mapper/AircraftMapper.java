package com.airlineservice.mapper;

import com.airlineportal.payload.request.Airlines.Aircraft.AircraftRequest;
import com.airlineportal.payload.response.Airlines.Aircraft.AircraftResponse;
import com.airlineportal.utils.Airline.AircraftStatus;
import com.airlineservice.model.Aircraft;
import com.airlineservice.model.Airline;


public class AircraftMapper {

    // Request -> Entity
    public static Aircraft toEntity(AircraftRequest aircraftRequest, Airline airline){
        if(aircraftRequest == null){
            throw new IllegalArgumentException("Aircraft request cannot be null");
        }

        if(airline == null){
            throw new IllegalArgumentException("Airline cannot be null");
        }

        return Aircraft.builder()
                .code(aircraftRequest.getCode().toUpperCase())
                .model(aircraftRequest.getModel())
                .manufacturer(aircraftRequest.getManufacturer())

                .seatingCapacity(aircraftRequest.getSeatingCapacity())
                .economySeats(safeInt(aircraftRequest.getEconomySeats()))
                .premiumEconomySeats(safeInt(aircraftRequest.getPremiumEconomySeats()))
                .businessSeats(safeInt(aircraftRequest.getBusinessSeats()))
                .firstClassSeats(safeInt(aircraftRequest.getFirstClassSeats()))

                .rangeKm(aircraftRequest.getRangeKm())
                .cruisingSpeedKmh(aircraftRequest.getCruisingSpeedKmh())
                .maxAltitudeFt(aircraftRequest.getMaxAltitudeFt())
                .yearOfManufacture(aircraftRequest.getYearOfManufacture())

                .registrationDate(aircraftRequest.getRegistrationDate())
                .nextMaintenanceDate(aircraftRequest.getNextMaintenanceDate())

                .aircraftStatus(aircraftRequest.getAircraftStatus() != null ? aircraftRequest.getAircraftStatus() : AircraftStatus.ACTIVE)
                .isAvailable(aircraftRequest.getIsAvailable() != null ? aircraftRequest.getIsAvailable() : true)

                .currentAirportId(aircraftRequest.getCurrentAirportId())
                .airline(airline)
                .build();
    }

    // Update Entity
    public static void updateEntity(Aircraft aircraft, AircraftRequest aircraftRequest){
        if(aircraft == null){
            throw new IllegalArgumentException("Aircraft cannot be null");
        }

        if(aircraftRequest == null){
            throw new IllegalArgumentException("Aircraft request cannot be null");
        }


        aircraft.setCode(normalizeCode(aircraftRequest.getCode()));
        aircraft.setModel(normalizeText(aircraftRequest.getModel()));
        aircraft.setManufacturer(normalizeText(aircraftRequest.getManufacturer()));

        aircraft.setSeatingCapacity(aircraftRequest.getSeatingCapacity());
        aircraft.setEconomySeats(safeInt(aircraftRequest.getEconomySeats()));
        aircraft.setPremiumEconomySeats(safeInt(aircraftRequest.getPremiumEconomySeats()));
        aircraft.setBusinessSeats(safeInt(aircraftRequest.getBusinessSeats()));
        aircraft.setFirstClassSeats(safeInt(aircraftRequest.getFirstClassSeats()));

        aircraft.setRangeKm(aircraftRequest.getRangeKm());
        aircraft.setCruisingSpeedKmh(aircraftRequest.getCruisingSpeedKmh());
        aircraft.setMaxAltitudeFt(aircraftRequest.getMaxAltitudeFt());
        aircraft.setYearOfManufacture(aircraftRequest.getYearOfManufacture());

        aircraft.setRegistrationDate(aircraftRequest.getRegistrationDate());
        aircraft.setNextMaintenanceDate(aircraftRequest.getNextMaintenanceDate());

        if(aircraftRequest.getAircraftStatus() != null) {
            aircraft.setAircraftStatus(aircraftRequest.getAircraftStatus());
        }
        if(aircraftRequest.getIsAvailable() != null) {
            aircraft.setIsAvailable(aircraftRequest.getIsAvailable());
        }
        aircraft.setCurrentAirportId(aircraftRequest.getCurrentAirportId());
    }

    // Entity -> Response
    public static AircraftResponse toResponse(Aircraft aircraft){
        if(aircraft == null)
            return null;

        Airline airline = aircraft.getAirline();

        return AircraftResponse.builder()
                .id(aircraft.getId())
                .code(aircraft.getCode())
                .model(aircraft.getModel())
                .manufacturer(aircraft.getManufacturer())

                .seatingCapacity(aircraft.getSeatingCapacity())
                .economySeats(aircraft.getEconomySeats())
                .premiumEconomySeats(aircraft.getPremiumEconomySeats())
                .businessSeats(aircraft.getBusinessSeats())
                .firstClassSeats(aircraft.getFirstClassSeats())

                .rangeKm(aircraft.getRangeKm())
                .cruisingSpeedKmh(aircraft.getCruisingSpeedKmh())
                .maxAltitudeFt(aircraft.getMaxAltitudeFt())
                .yearOfManufacture(aircraft.getYearOfManufacture())

                .registrationDate(aircraft.getRegistrationDate())
                .nextMaintenanceDate(aircraft.getNextMaintenanceDate())

                .aircraftStatus(aircraft.getAircraftStatus())
                .isAvailable(aircraft.getIsAvailable())

                .airlineId(airline != null ? airline.getId() : null)
                .airlineName(airline != null ? airline.getName() : null)
                .airlineIataCode(airline != null ? airline.getIataCode() : null)

                .currentAirportId(aircraft.getCurrentAirportId())

                .totalSeats(aircraft.getTotalSeats())
                .requiresMaintenance(aircraft.getRequiresMaintenance())
                .isOperational(aircraft.getOperational())

                .createdAt(aircraft.getCreatedAt())
                .updatedAt(aircraft.getUpdatedAt())

                .build();
    }


    private static String normalizeCode(String code){
        if(code == null){
            return null;
        }
        return code.trim().toUpperCase();
    }

    private static String normalizeText(String value){
        if(value == null){
            return null;
        }
        return value.trim();
    }


    private static int safeInt(Integer value){
        return value == null ? 0 : value;
    }


}
