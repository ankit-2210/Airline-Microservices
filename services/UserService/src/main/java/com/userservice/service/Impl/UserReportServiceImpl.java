package com.userservice.service.Impl;

import com.userservice.model.User;
import com.userservice.repository.UserRepository;
import com.userservice.service.UserReportService;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserReportServiceImpl implements UserReportService {
    private final UserRepository userRepository;

    @Override
    public byte[] generateUsersPdf() {
        try {
            List<User> users = userRepository.findAll();

            ClassPathResource reportResource = new ClassPathResource("reports/users.jasper");
            try (InputStream reportStream = reportResource.getInputStream()) {
                JasperReport jasperReport = (JasperReport) JRLoader.loadObject(reportStream);

                Map<String, Object> parameters = new HashMap<>();
                parameters.put("REPORT_TITLE", "User Report");

                JasperPrint jasperPrint = JasperFillManager.fillReport(
                        jasperReport,
                        parameters,
                        new net.sf.jasperreports.engine.data.JRBeanCollectionDataSource(
                                users
                        )
                );

                return JasperExportManager.exportReportToPdf(jasperPrint);
            }
        }
        catch (JRException e) {
            throw new RuntimeException("Failed to generate user PDF report", e);
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to load user PDF report", e);
        }


    }
}
