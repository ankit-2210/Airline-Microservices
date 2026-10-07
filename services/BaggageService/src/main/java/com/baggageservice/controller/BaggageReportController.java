package com.baggageservice.controller;

import com.baggageservice.service.BaggageReportService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/baggage/reports")
public class BaggageReportController {
    private final BaggageReportService baggageReportService;

    @GetMapping("/pdf")
    public ResponseEntity<byte[]> generateBookingsPdf(){
        byte[] pdf = baggageReportService.generateBaggagePdf();

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=baggage_report.pdf"
                )
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(pdf.length)
                .body(pdf);
    }

    @GetMapping("/booking/{bookingId}/pdf")
    public ResponseEntity<byte[]> generateBookingBaggagePdf(@PathVariable Long bookingId){
        byte[] pdf = baggageReportService.generateBookingBaggagePdf(bookingId);

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=booking_" + bookingId + "_baggage.pdf"
                )
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(pdf.length)
                .body(pdf);
    }

    @GetMapping("/passenger/{passengerId}/pdf")
    public ResponseEntity<byte[]> generatePassengerBaggagePdf(@PathVariable Long passengerId){
        byte[] pdf = baggageReportService.generatePassengerBaggagePdf(passengerId);

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=passenger_" + passengerId + "_baggage.pdf"
                )
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(pdf.length)
                .body(pdf);
    }

    @GetMapping("/flight-instance/{flightInstanceId}/pdf")
    public ResponseEntity<byte[]> generateFlightInstanceBaggagePdf(@PathVariable Long flightInstanceId){
        byte[] pdf = baggageReportService.generateFlightInstanceBaggagePdf(flightInstanceId);

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=flight_instance_" + flightInstanceId + "_baggage.pdf"
                )
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(pdf.length)
                .body(pdf);
    }



}
