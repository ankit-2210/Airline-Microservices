package com.bookingservice.service.Impl;

import com.bookingservice.dto.BookingReportData;
import com.bookingservice.dto.PassengerReportData;
import com.bookingservice.mapper.BookingReportMapper;
import com.bookingservice.model.Booking;
import com.bookingservice.model.Passenger;
import com.bookingservice.repository.BookingRepository;
import com.bookingservice.repository.PassengerRepository;
import com.bookingservice.service.BookingReportService;
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
public class BookingReportServiceImpl implements BookingReportService {
    private final BookingRepository bookingRepository;
    private final PassengerRepository passengerRepository;

    @Override
    public byte[] generateBookingsPdf() {
        List<Booking> bookings = bookingRepository.findAll();

        List<BookingReportData> reportData = bookings.stream()
                .map(BookingReportMapper::toBookingReportData)
                .toList();

        return generatePdf(
                "reports/bookings.jrxml",
                "BOOKING MANAGEMENT REPORT",
                reportData,
                "Failed to generate booking PDF report"
        );

    }

    @Override
    public byte[] generatePassengersPdf() {
        List<Passenger> passengers = passengerRepository.findAll();

        List<PassengerReportData> reportData = passengers.stream()
                .map(BookingReportMapper::toPassengerReportData)
                .toList();

        return generatePdf(
                "reports/passengers.jrxml",
                "PASSENGER MANIFEST REPORT",
                reportData,
                "Failed to generate passenger PDF report"
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

            throw new RuntimeException("Failed to generate booking PDF report", e);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load booking PDF report", e);
        }

    }
}
