package com.seatservice.controller;

import com.seatservice.service.SeatReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/seats/reports")
public class SeatReportController {
    private final SeatReportService seatReportService;

    @GetMapping("/pdf")
    public ResponseEntity<byte[]> generateRefundsPdf(){
        byte[] pdf = seatReportService.generateSeatsPdf();

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=seat-report.pdf"
                )
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(pdf.length)
                .body(pdf);
    }


}
