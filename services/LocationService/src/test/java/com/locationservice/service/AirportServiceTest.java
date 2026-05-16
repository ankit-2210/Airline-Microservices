package com.locationservice.service;

import com.locationservice.model.Airport;
import com.locationservice.model.City;
import com.locationservice.repository.AirportRepository;
import com.locationservice.repository.CityRepository;
import com.locationservice.service.Impl.AirportServiceImpl;
import com.microservices.payload.request.Airport.AirportRequest;
import com.microservices.payload.response.Airport.AirportResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.concurrent.Callable;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AirportServiceTest {
    @Mock
    private AirportRepository airportRepository;

    @Mock
    private CityRepository cityRepository;

    @InjectMocks
    private AirportServiceImpl airportService;

    @Test
    void createAirport_success(){
        AirportRequest airportRequest = new AirportRequest();
        airportRequest.setIataCode("CCU");
        airportRequest.setName("Kolkata Airport");
        airportRequest.setCityId(1L);
        City city = City.builder().id(1L).build();

        when(airportRepository.findByIataCode("CCU"))
                .thenReturn(Optional.empty());
        when(cityRepository.findById(1L))
                .thenReturn(Optional.of(city));

        when(airportRepository.save(any(Airport.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        AirportResponse airportResponse = airportService.createAirport(airportRequest);

        assertNotNull(airportResponse);
        verify(airportRepository).save(any(Airport.class));
    }



}
