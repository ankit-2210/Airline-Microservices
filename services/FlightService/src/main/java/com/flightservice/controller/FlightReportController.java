package com.flightservice.controller;

import com.flightservice.service.FlightReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reports")
public class FlightReportController {
    private final FlightReportService reportService;

    @GetMapping("/flights/pdf")
    public ResponseEntity<byte[]> generateFlightsPdf(){
        byte[] pdf = reportService.generateFlightsPdf();

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=flights-report.pdf"
                )
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(pdf.length)
                .body(pdf);
    }

    @GetMapping("/flight-schedules/pdf")
    public ResponseEntity<byte[]> generateFlightSchedulesPdf(){
        byte[] pdf = reportService.generateFlightSchedulesPdf();

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=flight-schedules-report.pdf"
                )
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(pdf.length)
                .body(pdf);
    }

    @GetMapping("/flight-instances/pdf")
    public ResponseEntity<byte[]> generateFlightInstancesPdf(){
        byte[] pdf = reportService.generateFlightInstancesPdf();

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=flight-instances-report.pdf"
                )
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(pdf.length)
                .body(pdf);
    }




}
