package com.bookingservice.controller;


import com.airlineportal.payload.request.Booking.BookingRequest;
import com.airlineportal.payload.response.ApiResponse;
import com.airlineportal.payload.response.Booking.BookingResponse;
import com.bookingservice.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookings")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public ApiResponse<BookingResponse> createBooking(@Valid @RequestBody BookingRequest request){
        return ApiResponse.success(bookingService.createBooking(request));
    }

    @GetMapping("/{bookingId}")
    public ApiResponse<BookingResponse> getById(@PathVariable Long bookingId){
        return ApiResponse.success(bookingService.getById(bookingId));
    }

    @GetMapping("/pnr/{pnr}")
    public ApiResponse<BookingResponse> getByPnr(@PathVariable String pnr){
        return ApiResponse.success(bookingService.getByPnr(pnr));
    }

    @GetMapping("/user/{userId}")
    public ApiResponse<Page<BookingResponse>> getByUser(@PathVariable Long userId, Pageable pageable){
        return ApiResponse.success(bookingService.getByUser(userId, pageable));
    }

    @GetMapping("/flight/{flightId}")
    public ApiResponse<Page<BookingResponse>> getByFlight(@PathVariable Long flightId, Pageable pageable){
        return ApiResponse.success(bookingService.getByFlight(flightId, pageable));
    }

    @GetMapping
    public ApiResponse<Page<BookingResponse>> getAll(Pageable pageable){
        return ApiResponse.success(bookingService.getAll(pageable));
    }

    @PatchMapping("/{bookingId}/cancel")
    public ApiResponse<BookingResponse> cancelBooking(@PathVariable Long bookingId, @RequestParam Long userId, @RequestParam(required = false) String reason){
        return ApiResponse.success(bookingService.cancelBooking(bookingId, userId, reason));
    }


}
