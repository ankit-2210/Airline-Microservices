package com.locationservice.service;

import com.microservices.payload.request.Location.City.CityRequest;
import com.microservices.payload.response.Location.City.CityResponse;
import org.springframework.data.domain.*;
import java.util.*;

public interface CityService {

    CityResponse createCity(CityRequest cityRequest);
    CityResponse getCityById(Long id);
    CityResponse getCityByCode(String cityCode);
    CityResponse updateCity(Long id, CityRequest cityRequest);

    void deleteCity(Long id);

    Page<CityResponse> getAllCities(Pageable pageable);
    Page<CityResponse> searchCities(String keyword, Pageable pageable);
    Page<CityResponse> getCitiesByCountryCode(String countryCode, Pageable pageable);

    List<CityResponse> getCityDropdown();

    boolean cityExists(String cityCode);

}
