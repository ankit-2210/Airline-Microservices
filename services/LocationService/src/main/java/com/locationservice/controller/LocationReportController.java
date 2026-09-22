package com.locationservice.controller;


import com.locationservice.service.LocationReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/location/reports")
public class LocationReportController {
    private final LocationReportService locationReportService;

    @GetMapping("/airports/pdf")
    public ResponseEntity<byte[]> generateAirportsPdf() {
        byte[] pdf = locationReportService.generateAirportsPdf();

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=airports-report.pdf"
                )
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(pdf.length)
                .body(pdf);
    }


    @GetMapping("/cities/pdf")
    public ResponseEntity<byte[]> generateCitiesPdf() {
        byte[] pdf = locationReportService.generateCitiesPdf();

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=cities-report.pdf"
                )
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(pdf.length)
                .body(pdf);
    }

}
