package com.seatservice.service.Impl;

import com.seatservice.dto.SeatReportData;
import com.seatservice.mapper.SeatMapper;
import com.seatservice.model.Seat;
import com.seatservice.repository.SeatRepository;
import com.seatservice.service.SeatReportService;
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
public class SeatReportServiceImpl implements SeatReportService {
    private final SeatRepository seatRepository;

    @Override
    public byte[] generateSeatsPdf() {
        try {
            List<Seat> seats = seatRepository.findAll();

            List<SeatReportData> reportData = seats.stream()
                    .map(SeatMapper::toReportData)
                    .toList();

            ClassPathResource resource = new ClassPathResource("reports/seat_report.jrxml");
            if (!resource.exists()) {
                throw new RuntimeException("seats_report.jrxml not found in src/main/resources/reports/");
            }

            try (InputStream inputStream = resource.getInputStream()) {
                System.out.println("Jasper template found: " + resource.getDescription());

                JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);

                Map<String, Object> parameters = new HashMap<>();
                parameters.put("REPORT_TITLE", "SEAT Management Report");

                JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(reportData);
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
                return JasperExportManager.exportReportToPdf(jasperPrint);
            }
        }
        catch (JRException e) {
            System.err.println("========== SEAT JASPER ERROR ==========");
            e.printStackTrace();

            Throwable cause = e.getCause();

            while (cause != null) {
                System.err.println("========== CAUSED BY ==========");
                cause.printStackTrace();
                cause = cause.getCause();
            }

            throw new RuntimeException("Failed to generate seat PDF report", e);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load seat PDF report", e);
        }
    }



}
