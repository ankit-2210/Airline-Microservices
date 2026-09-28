package com.flightservice.service.Impl;

import com.flightservice.dto.FlightInstanceReportData;
import com.flightservice.dto.FlightReportData;
import com.flightservice.dto.FlightScheduleReportData;
import com.flightservice.mapper.FlightInstanceMapper;
import com.flightservice.mapper.FlightMapper;
import com.flightservice.mapper.FlightScheduleMapper;
import com.flightservice.model.Flight;
import com.flightservice.model.FlightInstance;
import com.flightservice.model.FlightSchedule;
import com.flightservice.repository.FlightInstanceRepository;
import com.flightservice.repository.FlightRepository;
import com.flightservice.repository.FlightScheduleRepository;
import com.flightservice.service.FlightInstanceService;
import com.flightservice.service.FlightReportService;
import com.flightservice.service.FlightScheduleService;
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
public class FlightReportServiceImpl implements FlightReportService {
    private final FlightRepository flightRepository;
    private final FlightScheduleRepository flightScheduleRepository;
    private final FlightInstanceRepository flightInstanceRepository;

    @Override
    public byte[] generateFlightsPdf() {
        List<Flight> flights = flightRepository.findAll();

        List<FlightReportData> reportData = flights.stream()
                .map(FlightMapper::toReportData)
                .toList();

        return generatePdf(
                "reports/flights.jrxml",
                "Flight Management Report",
                reportData,
                "Failed to generate flight PDF report"
        );

    }

    @Override
    public byte[] generateFlightSchedulesPdf() {
        List<FlightSchedule> schedules = flightScheduleRepository.findAll();

        List<FlightScheduleReportData> reportData = schedules.stream()
                .map(FlightScheduleMapper::toReportData)
                .toList();

        return generatePdf(
                "reports/flight_schedules.jrxml",
                "Flight Schedule Management Report",
                reportData,
                "Failed to generate flight schedule PDF report"
        );
    }

    @Override
    public byte[] generateFlightInstancesPdf() {
        List< FlightInstance> instances = flightInstanceRepository.findAll();

        List<FlightInstanceReportData> reportData = instances.stream()
                .map(FlightInstanceMapper::toReportData)
                .toList();

        return generatePdf(
                "reports/flight_instances.jrxml",
                "Flight Instance Management Report",
                reportData,
                "Failed to generate flight instance PDF report"
        );
    }



    private byte[] generatePdf(String reportPath, String title, List<?> reportData, String errorMessage){
        try{
            ClassPathResource resource = new ClassPathResource(reportPath);

            try (InputStream inputStream = resource.getInputStream()) {
                System.out.println("Jasper template found: " + resource.getDescription());

                JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);

                Map<String, Object> parameters = new HashMap<>();
                parameters.put("REPORT_TITLE", title);

                JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(reportData);
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
                return JasperExportManager.exportReportToPdf(jasperPrint);
            }
        }
        catch (JRException e) {
            System.err.println("========== FIGHT JASPER ERROR ==========");
            e.printStackTrace();

            Throwable cause = e.getCause();

            while (cause != null) {
                System.err.println("========== CAUSED BY ==========");
                cause.printStackTrace();
                cause = cause.getCause();
            }

            throw new RuntimeException("Failed to generate flight PDF report", e);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load flight PDF report", e);
        }

    }

}
