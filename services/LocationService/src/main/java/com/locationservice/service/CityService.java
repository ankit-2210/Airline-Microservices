package com.locationservice.service;

import com.microservices.payload.request.City.CityRequest;
import com.microservices.payload.response.City.CityResponse;
import org.springframework.data.domain.*;


public interface CityService {

    CityResponse createCity(CityRequest cityRequest) throws Exception;
    CityResponse getCityById(Long id) throws Exception;
    CityResponse updateCity(Long id, CityRequest cityRequest) throws Exception;
    void deleteCity(Long id) throws Exception;

    Page<CityResponse> getAllCities(Pageable pageable);
    Page<CityResponse> searchCities(String keyword, Pageable pageable);
    Page<CityResponse> getCitiesByCountryCode(String countryCode, Pageable pageable);

    boolean cityExists(String cityCode);

}
