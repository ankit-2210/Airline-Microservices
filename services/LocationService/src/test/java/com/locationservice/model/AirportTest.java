package com.locationservice.model;

import org.junit.jupiter.api.Test;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.*;


public class AirportTest {
    @Test
    void testAirportCreation(){
        City city = City.builder()
                .id(1L)
                .name("Kolkata")
                .cityCode("KOL")
                .countryCode("IN")
                .countryName("India")
                .build();

        Airport airport = Airport.builder()
                .iataCode("CCU")
                .name("Netaji Subhas Chandra Bose Airport")
                .timeZoneId(ZoneId.of("Asia/Kolkata"))
                .city(city)
                .build();

        assertNotNull(airport);
        assertEquals("CCU", airport.getIataCode());
        assertEquals("Asia/Kolkata", airport.getTimeZoneId().toString());
    }

    @Test
    void testDetailedName(){
        City city = City.builder()
                .countryCode("KOL")
                .regionCode("IN")
                .build();
        Airport airport = Airport.builder()
                .name("Kolkata Airport")
                .city(city)
                .build();
        String result = airport.getDetailedName();
        assertEquals("KOLKATA AIRPORT/KOL", result);
    }
}
