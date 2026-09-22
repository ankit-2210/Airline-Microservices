package com.locationservice.service.Impl;

import com.locationservice.model.Airport;
import com.locationservice.model.City;
import com.locationservice.repository.AirportRepository;
import com.locationservice.repository.CityRepository;
import com.locationservice.service.LocationReportService;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LocationReportServiceImpl implements LocationReportService {

    private final AirportRepository airportRepository;
    private final CityRepository cityRepository;

    @Override
    public byte[] generateAirportsPdf() {
        try {
            List<Airport> airports = airportRepository.findAll();

            ClassPathResource resource = new ClassPathResource("reports/airports.jrxml");
            if (!resource.exists()) {
                throw new RuntimeException("airports.jrxml not found in src/main/resources/reports/");
            }

            try (InputStream inputStream = resource.getInputStream()) {
                System.out.println("Jasper template found: " + resource.getDescription());

                JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);

                Map<String, Object> parameters = new HashMap<>();
                parameters.put("REPORT_TITLE", "Airport Management Report");

                JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(airports);
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
                return JasperExportManager.exportReportToPdf(jasperPrint);
            }
        }
        catch (JRException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to generate airport PDF report", e);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load airport PDF report", e);
        }
    }

    @Override
    public byte[] generateCitiesPdf() {
        try {
            List<City> cities = cityRepository.findAll();

            ClassPathResource resource = new ClassPathResource("reports/cities.jrxml");
            if (!resource.exists()) {
                throw new RuntimeException("cities.jrxml not found in src/main/resources/reports/");
            }

            try (InputStream inputStream = resource.getInputStream()) {
                System.out.println("Jasper template found: " + resource.getDescription());

                JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);

                Map<String, Object> parameters = new HashMap<>();
                parameters.put("REPORT_TITLE", "City Management Report");

                JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(cities);
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
                return JasperExportManager.exportReportToPdf(jasperPrint);
            }
        }
        catch (JRException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to generate city PDF report", e);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load city PDF report", e);
        }
    }
}
