package com.paymentservice.service.Impl;

import com.paymentservice.dto.PaymentReportData;
import com.paymentservice.mapper.PaymentReportMapper;
import com.paymentservice.model.Payment;
import com.paymentservice.repository.PaymentRepository;
import com.paymentservice.service.PaymentReportService;
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
public class PaymentReportServiceImpl implements PaymentReportService {
    private final PaymentRepository paymentRepository;

    @Override
    public byte[] generatePaymentPdf() {
        List<Payment> payments = paymentRepository.findAll();

        List<PaymentReportData> reportData = payments.stream()
                .map(PaymentReportMapper::toReportData)
                .toList();

        return generatePdf(
                "reports/payments.jrxml",
                "PAYMENT MANAGEMENT REPORT",
                reportData,
                "Failed to generate payment PDF report"
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
