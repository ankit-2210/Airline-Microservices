package com.paymentservice.controller;

import com.paymentservice.service.PaymentReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reports")
public class PaymentReportController {
    private final PaymentReportService paymentReportService;

    @GetMapping("/payments/pdf")
    public ResponseEntity<byte[]> generatePaymentPdf(){
        byte[] pdf = paymentReportService.generatePaymentPdf();

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=payments-report.pdf"
                )
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(pdf.length)
                .body(pdf);
    }

}
