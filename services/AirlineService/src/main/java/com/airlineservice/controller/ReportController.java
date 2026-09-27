package com.airlineservice.controller;


import com.airlineservice.service.AirlineAircraftReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/airline/reports")
public class ReportController {
    private final AirlineAircraftReportService reportService;

    // Airline PDF Report
    @GetMapping("/airlines/pdf")
    public ResponseEntity<byte[]> generateAirlinesPdf() {
        byte[] pdf = reportService.generateAirlinesPdf();

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=airlines-report.pdf"
                )
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(pdf.length)
                .body(pdf);
    }

    // Airline PDF Report
    @GetMapping("/aircrafts/pdf")
    public ResponseEntity<byte[]> generateAircraftPdf() {
        byte[] pdf = reportService.generateAircraftPdf();

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=aircraft-report.pdf"
                )
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(pdf.length)
                .body(pdf);
    }

}
