package com.baggageservice.service.Impl;

import com.baggageservice.dto.BaggageReportData;
import com.baggageservice.mapper.BaggageMapper;
import com.baggageservice.model.Baggage;
import com.baggageservice.repository.BaggageRepository;
import com.baggageservice.service.BaggageReportService;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BaggageReportServiceImpl implements BaggageReportService {
    private final BaggageRepository baggageRepository;

    @Override
    public byte[] generateBaggagePdf() {
        List<Baggage> baggages = baggageRepository.findAll();

        List<BaggageReportData> reportData = baggages.stream()
                .map(BaggageMapper::toReportData)
                .toList();

        return generatePdf(
                "reports/baggage_report.jrxml",
                "BAGGAGE MANAGEMENT REPORT",
                reportData,
                "Failed to generate baggages PDF report"
        );
    }

    @Override
    public byte[] generateBookingBaggagePdf(Long bookingId) {
        List<Baggage> baggages = baggageRepository.findByBookingId(bookingId);

        List<BaggageReportData> reportData = baggages.stream()
                .map(BaggageMapper::toReportData)
                .toList();

        return generatePdf(
                "reports/baggage_report.jrxml",
                "BAGGAGE MANAGEMENT REPORT",
                reportData,
                "Failed to generate baggages PDF report"
        );
    }

    @Override
    public byte[] generatePassengerBaggagePdf(Long passengerId) {
        List<Baggage> baggages = baggageRepository.findByPassengerId(passengerId);

        List<BaggageReportData> reportData = baggages.stream()
                .map(BaggageMapper::toReportData)
                .toList();

        return generatePdf(
                "reports/baggage_report.jrxml",
                "BAGGAGE MANAGEMENT REPORT",
                reportData,
                "Failed to generate baggages PDF report"
        );
    }

    @Override
    public byte[] generateFlightInstanceBaggagePdf(Long flightInstanceId) {
        List<Baggage> baggages = baggageRepository.findByFlightInstanceId(flightInstanceId);

        List<BaggageReportData> reportData = baggages.stream()
                .map(BaggageMapper::toReportData)
                .toList();

        return generatePdf(
                "reports/baggage_report.jrxml",
                "BAGGAGE MANAGEMENT REPORT",
                reportData,
                "Failed to generate baggages PDF report"
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
            System.err.println("========== BAGGAGE JASPER ERROR ==========");
            e.printStackTrace();

            Throwable cause = e.getCause();

            while (cause != null) {
                System.err.println("========== CAUSED BY ==========");
                cause.printStackTrace();
                cause = cause.getCause();
            }

            throw new RuntimeException("Failed to generate baggage PDF report", e);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load baggage PDF report", e);
        }

    }

}
