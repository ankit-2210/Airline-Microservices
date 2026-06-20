package com.airlineservice.mapper;

import com.airlineservice.model.Aircraft;
import com.airlineservice.model.Airline;
import com.microservices.payload.request.Airlines.Aircraft.AircraftRequest;
import com.microservices.payload.response.Airlines.Aircraft.AircraftResponse;
import com.microservices.utils.Airline.AircraftStatus;

public class AircraftMapper {
    public static Aircraft toEntity(AircraftRequest aircraftRequest, Airline airline){
        int economySeats = aircraftRequest.getEconomySeats() == null ? 0 : aircraftRequest.getEconomySeats();
        int premiumEconomySeats = aircraftRequest.getPremiumEconomySeats() == null ? 0 : aircraftRequest.getPremiumEconomySeats();
        int businessSeats = aircraftRequest.getBusinessSeats() == null ? 0 : aircraftRequest.getBusinessSeats();
        int firstClassSeats = aircraftRequest.getFirstClassSeats() == null ? 0 : aircraftRequest.getFirstClassSeats();

        int total = economySeats + premiumEconomySeats + businessSeats + firstClassSeats;
        if(total == 0){
            total = aircraftRequest.getSeatingCapacity();
        }

        return Aircraft.builder()
                .code(aircraftRequest.getCode().toUpperCase())
                .model(aircraftRequest.getModel())
                .manufacturer(aircraftRequest.getManufacturer())

                .seatingCapacity(total)

                .economySeats(economySeats)
                .premiumEconomySeats(premiumEconomySeats)
                .businessSeats(businessSeats)
                .firstClassSeats(firstClassSeats)

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

    public static void updateEntity(Aircraft aircraft, AircraftRequest aircraftRequest){

        aircraft.setCode(aircraftRequest.getCode().toUpperCase());
        aircraft.setModel(aircraftRequest.getModel());
        aircraft.setManufacturer(aircraftRequest.getManufacturer());

        aircraft.setEconomySeats(aircraftRequest.getEconomySeats() == null ? 0 : aircraftRequest.getEconomySeats());
        aircraft.setPremiumEconomySeats(aircraftRequest.getPremiumEconomySeats() == null ? 0 : aircraftRequest.getPremiumEconomySeats());
        aircraft.setBusinessSeats(aircraftRequest.getBusinessSeats() == null ? 0 : aircraftRequest.getBusinessSeats());
        aircraft.setFirstClassSeats(aircraftRequest.getFirstClassSeats() == null ? 0 : aircraftRequest.getFirstClassSeats());

        aircraft.setSeatingCapacity(aircraft.getTotalSeats());

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

    public static AircraftResponse toResponse(Aircraft aircraft){
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

                .airlineId(aircraft.getAirline().getId())
                .airlineName(aircraft.getAirline().getName())
                .airlineIataCode(aircraft.getAirline().getIataCode())

                .currentAirportId(aircraft.getCurrentAirportId())

                .totalSeats(aircraft.getTotalSeats())
                .requiresMaintenance(aircraft.getRequiresMaintenance())
                .isOperational(aircraft.getOperational())

                .createdAt(aircraft.getCreatedAt())
                .updatedAt(aircraft.getUpdatedAt())

                .build();
    }

}
