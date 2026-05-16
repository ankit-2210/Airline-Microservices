package com.locationservice.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CityTest {
    @Test
    void testCityCreation(){
        City city = City.builder()
                .name("Kolkata")
                .cityCode("KOL")
                .countryCode("IN")
                .countryName("India")
                .regionCode("WB")
                .timeZoneId("Asia/Kolkata")
                .build();

        assertNotNull(city);
        assertEquals("KOL", city.getCityCode());
        assertEquals("India", city.getCountryName());
    }

}
