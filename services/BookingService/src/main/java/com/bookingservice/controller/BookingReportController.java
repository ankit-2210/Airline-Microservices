package com.bookingservice.controller;


import com.bookingservice.service.BookingReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reports")
public class BookingReportController {
    private final BookingReportService bookingReportService;

    @GetMapping("/bookings/pdf")
    public ResponseEntity<byte[]> generateBookingsPdf(){
        byte[] pdf = bookingReportService.generateBookingsPdf();

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=bookings-report.pdf"
                )
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(pdf.length)
                .body(pdf);
    }

    @GetMapping("/passengers/pdf")
    public ResponseEntity<byte[]> generatePassengersPdf(){
        byte[] pdf = bookingReportService.generatePassengersPdf();

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=passengers-report.pdf"
                )
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(pdf.length)
                .body(pdf);
    }



}
