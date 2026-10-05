package com.checkinservice.service.Impl;

import com.checkinservice.dto.CheckInReportData;
import com.checkinservice.mapper.CheckInMapper;
import com.checkinservice.model.CheckIn;
import com.checkinservice.repository.CheckInRepository;
import com.checkinservice.service.CheckInReportService;
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
public class CheckInReportServiceImpl implements CheckInReportService {
    private final CheckInRepository checkInRepository;

    @Override
    public byte[] generateCheckInsPdf() {
        List<CheckIn> checkIns = checkInRepository.findAll();

        List<CheckInReportData> reportData = checkIns.stream()
                .map(CheckInMapper::toReportData)
                .toList();

        return generatePdf(
                "reports/checkins_report.jrxml",
                "CHECKINS MANAGEMENT REPORT",
                reportData,
                "Failed to generate checkins PDF report"
        );
    }

    @Override
    public byte[] generateBookingCheckInsPdf(Long bookingId) {
        List<CheckIn> checkIns = checkInRepository.findAllByBookingId(bookingId);

        List<CheckInReportData> reportData = checkIns.stream()
                .map(CheckInMapper::toReportData)
                .toList();

        return generatePdf(
                "reports/checkins_report.jrxml",
                "CHECKINS MANAGEMENT REPORT",
                reportData,
                "Failed to generate checkins PDF report"
        );

    }

    @Override
    public byte[] generateFlightInstanceCheckInsPdf(Long flightInstanceId) {
        List<CheckIn> checkIns = checkInRepository.findAllByFlightInstanceId(flightInstanceId);

        List<CheckInReportData> reportData = checkIns.stream()
                .map(CheckInMapper::toReportData)
                .toList();

        return generatePdf(
                "reports/checkins_report.jrxml",
                "CHECKINS MANAGEMENT REPORT",
                reportData,
                "Failed to generate checkins PDF report"
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
            System.err.println("========== BOOKING JASPER ERROR ==========");
            e.printStackTrace();

            Throwable cause = e.getCause();

            while (cause != null) {
                System.err.println("========== CAUSED BY ==========");
                cause.printStackTrace();
                cause = cause.getCause();
            }

            throw new RuntimeException("Failed to generate checkIn PDF report", e);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load checkIn PDF report", e);
        }

    }

}
