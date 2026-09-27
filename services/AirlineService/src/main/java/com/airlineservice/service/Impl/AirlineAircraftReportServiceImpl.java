package com.airlineservice.service.Impl;

import com.airlineservice.dto.AircraftReportData;
import com.airlineservice.dto.AirlineReportData;
import com.airlineservice.mapper.AircraftMapper;
import com.airlineservice.mapper.AirlineMapper;
import com.airlineservice.model.Aircraft;
import com.airlineservice.model.Airline;
import com.airlineservice.repository.AircraftRepository;
import com.airlineservice.repository.AirlineRepository;
import com.airlineservice.service.AirlineAircraftReportService;
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
public class AirlineAircraftReportServiceImpl implements AirlineAircraftReportService {
    private final AirlineRepository airlineRepository;
    private final AircraftRepository aircraftRepository;

    @Override
    public byte[] generateAirlinesPdf() {
        try {
            List<Airline> airlines = airlineRepository.findAll();

            List<AirlineReportData> reportData = airlines.stream()
                    .map(AirlineMapper::toReportData)
                    .toList();

            ClassPathResource resource = new ClassPathResource("reports/airlines.jrxml");
            if (!resource.exists()) {
                throw new RuntimeException("airlines.jrxml not found in src/main/resources/reports/");
            }

            try (InputStream inputStream = resource.getInputStream()) {
                System.out.println("Jasper template found: " + resource.getDescription());

                JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);

                Map<String, Object> parameters = new HashMap<>();
                parameters.put("REPORT_TITLE", "Airline Management Report");

                JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(reportData);
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
                return JasperExportManager.exportReportToPdf(jasperPrint);
            }

        }
        catch (JRException e) {
            System.err.println("========== AIRLINE JASPER ERROR ==========");
            e.printStackTrace();

            Throwable cause = e.getCause();

            while (cause != null) {
                System.err.println("========== CAUSED BY ==========");
                cause.printStackTrace();
                cause = cause.getCause();
            }

            throw new RuntimeException("Failed to generate airline PDF report", e);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load airline PDF report", e);
        }
    }

    @Override
    public byte[] generateAircraftPdf() {
        try {
            List<Aircraft> aircrafts = aircraftRepository.findAll();

            List<AircraftReportData> reportData = aircrafts.stream()
                    .map(AircraftMapper::toReportData)
                    .toList();

            ClassPathResource resource = new ClassPathResource("reports/aircraft.jrxml");
            if (!resource.exists()) {
                throw new RuntimeException("aircraft.jrxml not found in src/main/resources/reports/");
            }

            try (InputStream inputStream = resource.getInputStream()) {
                System.out.println("Jasper template found: " + resource.getDescription());

                JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);

                Map<String, Object> parameters = new HashMap<>();
                parameters.put("REPORT_TITLE", "Aircraft Management Report");

                JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(reportData);
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
                return JasperExportManager.exportReportToPdf(jasperPrint);
            }

        }
        catch (JRException e) {
            System.err.println("========== AIRCRAFT JASPER ERROR ==========");
            e.printStackTrace();

            Throwable cause = e.getCause();

            while (cause != null) {
                System.err.println("========== CAUSED BY ==========");
                cause.printStackTrace();
                cause = cause.getCause();
            }

            throw new RuntimeException("Failed to generate aircraft PDF report", e);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load aircraft PDF report", e);
        }
    }
}
