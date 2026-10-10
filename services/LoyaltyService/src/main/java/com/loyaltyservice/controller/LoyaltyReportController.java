package com.loyaltyservice.controller;


import com.loyaltyservice.service.LoyaltyReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/loyalty/reports")
public class LoyaltyReportController {
    private final LoyaltyReportService loyaltyReportService;

    @GetMapping("/pdf")
    public ResponseEntity<byte[]> generateRefundsPdf(){
        byte[] pdf = loyaltyReportService.generateLoyaltyPdf();

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=loyalty-report.pdf"
                )
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(pdf.length)
                .body(pdf);
    }

}
