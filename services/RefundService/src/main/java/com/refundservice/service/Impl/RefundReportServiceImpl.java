package com.refundservice.service.Impl;

import com.refundservice.dto.RefundReportData;
import com.refundservice.mapper.RefundMapper;
import com.refundservice.model.Refund;
import com.refundservice.repository.RefundRepository;
import com.refundservice.service.RefundReportService;
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
public class RefundReportServiceImpl implements RefundReportService {
    private final RefundRepository refundRepository;


    @Override
    public byte[] generateRefundsPdf() {
        try {
            List<Refund> refunds = refundRepository.findAll();

            List<RefundReportData> reportData = refunds.stream()
                    .map(RefundMapper::toReportData)
                    .toList();

            ClassPathResource resource = new ClassPathResource("reports/refund_report.jrxml");
            if (!resource.exists()) {
                throw new RuntimeException("refund_report.jrxml not found in src/main/resources/reports/");
            }

            try (InputStream inputStream = resource.getInputStream()) {
                System.out.println("Jasper template found: " + resource.getDescription());

                JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);

                Map<String, Object> parameters = new HashMap<>();
                parameters.put("REPORT_TITLE", "REFUND Management Report");

                JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(reportData);
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
                return JasperExportManager.exportReportToPdf(jasperPrint);
            }
        }
        catch (JRException e) {
            System.err.println("========== REFUND JASPER ERROR ==========");
            e.printStackTrace();

            Throwable cause = e.getCause();

            while (cause != null) {
                System.err.println("========== CAUSED BY ==========");
                cause.printStackTrace();
                cause = cause.getCause();
            }

            throw new RuntimeException("Failed to generate refund PDF report", e);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load refund PDF report", e);
        }
    }


}
