package com.loyaltyservice.service.Impl;


import com.loyaltyservice.dto.LoyaltyReportData;
import com.loyaltyservice.mapper.LoyaltyMapper;
import com.loyaltyservice.model.LoyaltyAccount;
import com.loyaltyservice.repository.LoyaltyAccountRepository;
import com.loyaltyservice.service.LoyaltyReportService;
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
public class LoyaltyReportServiceImpl implements LoyaltyReportService {
    private final LoyaltyAccountRepository loyaltyAccountRepository;

    @Override
    public byte[] generateLoyaltyPdf() {
        try {
            List<LoyaltyAccount> accounts = loyaltyAccountRepository.findAll();

            List<LoyaltyReportData> reportData = accounts.stream()
                    .map(LoyaltyMapper::toReportData)
                    .toList();

            ClassPathResource resource = new ClassPathResource("reports/loyalty_report.jrxml");
            if (!resource.exists()) {
                throw new RuntimeException("loyalty_report.jrxml not found in src/main/resources/reports/");
            }

            try (InputStream inputStream = resource.getInputStream()) {
                System.out.println("Jasper template found: " + resource.getDescription());

                JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);

                Map<String, Object> parameters = new HashMap<>();
                parameters.put("REPORT_TITLE", "LOYALTY Management Report");

                JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(reportData);
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
                return JasperExportManager.exportReportToPdf(jasperPrint);
            }
        }
        catch (JRException e) {
            System.err.println("========== LOYALTY JASPER ERROR ==========");
            e.printStackTrace();

            Throwable cause = e.getCause();

            while (cause != null) {
                System.err.println("========== CAUSED BY ==========");
                cause.printStackTrace();
                cause = cause.getCause();
            }

            throw new RuntimeException("Failed to generate loyalty PDF report", e);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load loyalty PDF report", e);
        }
    }


}
