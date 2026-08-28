package com.airlineservice.repository;

import com.airlineportal.utils.Airline.AircraftStatus;
import com.airlineservice.model.Aircraft;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AircraftRepository extends JpaRepository<Aircraft, Long> {

    // Aircraft Code
    Optional<Aircraft> findByCode(String code);

    boolean existsByCode(String code);
    boolean existsByCodeAndIdNot(String code, Long id);

    // Airline
    List<Aircraft> findByAirlineId(Long airlineId);
    Page<Aircraft> findByAirlineId(Long airlineId, Pageable pageable);
    Page<Aircraft> findByAirlineOwnerId(Long ownerId, Pageable pageable);

    // Airline + Aircraft Code
    Optional<Aircraft> findByAirlineIdAndCode(Long airlineId, String code);
    boolean existsByAirlineIdAndCode(Long airlineId, String code);
    boolean existsByAirlineIdAndCodeAndIdNot(Long airlineId, String code, Long id);

    // Status
    Page<Aircraft> findByAircraftStatus(AircraftStatus aircraftStatus, Pageable pageable);

    // Availability
    Page<Aircraft> findByIsAvailableTrue(Pageable pageable);
    Page<Aircraft> findByIsAvailableFalse(Pageable pageable);

    // Airport
    List<Aircraft> findByCurrentAirportId(Long airportId);
    Page<Aircraft> findByCurrentAirportId(Long airportId, Pageable pageable);

    // Search
    Page<Aircraft> findByCodeContainingIgnoreCaseOrModelContainingIgnoreCaseOrManufacturerContainingIgnoreCase(String code, String model, String manufacturer, Pageable pageable);

    // Maintenance
    List<Aircraft> findByNextMaintenanceDateBefore(LocalDate date);
    Page<Aircraft> findByNextMaintenanceDateBefore(LocalDate date, Pageable pageable);
    List<Aircraft> findByNextMaintenanceDateBetween(LocalDate startDate, LocalDate endDate);

    // Operational Aircraft
    List<Aircraft> findByAircraftStatusAndIsAvailableTrue(AircraftStatus aircraftStatus);
    Page<Aircraft> findByAircraftStatusAndIsAvailableTrue(AircraftStatus aircraftStatus, Pageable pageable);

    // Statistics
    long countByAirlineId(Long airlineId);
    long countByAirlineIdAndAircraftStatus(Long airlineId, AircraftStatus aircraftStatus);
    long countByAirlineIdAndIsAvailableTrue(Long airlineId);
    long countByAircraftStatus(AircraftStatus aircraftStatus);


}
