package com.refundservice.controller;

import com.refundservice.service.RefundReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/refunds/reports")
public class RefundReportController {
    private final RefundReportService refundReportService;

    @GetMapping("/pdf")
    public ResponseEntity<byte[]> generateRefundsPdf(){
        byte[] pdf = refundReportService.generateRefundsPdf();

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=refund-report.pdf"
                )
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(pdf.length)
                .body(pdf);
    }

}
