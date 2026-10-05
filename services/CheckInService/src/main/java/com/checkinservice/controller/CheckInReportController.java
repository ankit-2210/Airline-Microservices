package com.checkinservice.controller;


import com.checkinservice.service.CheckInReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/check-ins/reports")
public class CheckInReportController {
    private final CheckInReportService checkInReportService;

    @GetMapping("/pdf")
    public ResponseEntity<byte[]> generateBookingsPdf(){
        byte[] pdf = checkInReportService.generateCheckInsPdf();

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=checkins-report.pdf"
                )
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(pdf.length)
                .body(pdf);
    }

    @GetMapping("/booking/{bookingId}/pdf")
    public ResponseEntity<byte[]> generateBookingCheckInsPdf(@PathVariable Long bookingId){
        byte[] pdf = checkInReportService.generateBookingCheckInsPdf(bookingId);

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=booking_" + bookingId + "_checkins.pdf"
                )
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(pdf.length)
                .body(pdf);
    }

    @GetMapping("/flight-instance/{flightInstanceId}/pdf")
    public ResponseEntity<byte[]> generateFlightInstanceCheckInsPdf(@PathVariable Long flightInstanceId){
        byte[] pdf = checkInReportService.generateFlightInstanceCheckInsPdf(flightInstanceId);

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=flight_instance_" + flightInstanceId + "_checkins.pdf"
                )
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(pdf.length)
                .body(pdf);
    }




}
