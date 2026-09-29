package com.bookingservice.service.Impl;

import com.airlineportal.exception.ResourceNotFoundException;
import com.airlineportal.payload.request.Booking.BookingRequest;
import com.airlineportal.payload.response.ApiResponse;
import com.airlineportal.payload.response.Booking.BookingResponse;
import com.airlineportal.payload.response.Flight.FlightInstanceResponse;
import com.airlineportal.payload.response.Flight.FlightResponse;
import com.airlineportal.payload.response.User.UserResponse;
import com.airlineportal.utils.Booking.BookingStatus;
import com.airlineportal.utils.Booking.PaymentStatus;
import com.bookingservice.external.ExternalService;
import com.bookingservice.helper.BookingHelper;
import com.bookingservice.mapper.BookingMapper;
import com.bookingservice.model.Booking;
import com.bookingservice.repository.BookingRepository;
import com.bookingservice.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final BookingHelper bookingHelper;
    private final ExternalService externalService;

    @Override
    @Transactional
    public BookingResponse createBooking(BookingRequest request) {
        bookingHelper.validateCreate(request);

        int passengerCount = request.getPassengers().size();

        // Verify User
        ApiResponse<UserResponse> userResponse = externalService.getUserById(request.getUserId());
        if (userResponse == null || userResponse.getData() == null) {
            throw new ResourceNotFoundException("User not found with id: " + request.getUserId());
        }

        // Get Flight Instance
        ApiResponse<FlightInstanceResponse> instanceResponse = externalService.getInstanceById(request.getFlightInstanceId());
        if (instanceResponse == null || instanceResponse.getData() == null) {
            throw new ResourceNotFoundException("Flight instance not found with id: " + request.getFlightInstanceId());
        }

        FlightInstanceResponse instance = instanceResponse.getData();
        Long flightId = instance.getFlightId();
        if (flightId == null) {
            throw new IllegalArgumentException("Flight instance is not associated with a flight");
        }

        // Verify Flight
        ApiResponse<FlightResponse> flightResponse = externalService.getFlightById(flightId);
        if (flightResponse == null || flightResponse.getData() == null) {
            throw new ResourceNotFoundException("Flight not found with id: " + flightId);
        }

        // Reserve Seats
        ApiResponse<Boolean> reserveResponse = externalService.reserveSeats(request.getFlightInstanceId(), passengerCount);
        if (reserveResponse == null || !Boolean.TRUE.equals(reserveResponse.getData())) {
            throw new IllegalArgumentException("Unable to reserve seats");
        }

        String pnr = bookingHelper.generateUniquePnr();

        try {
            Booking booking = BookingMapper.toEntity(request, pnr, flightId);

            booking.setBookingStatus(BookingStatus.PENDING);
            booking.setPaymentStatus(PaymentStatus.PENDING);
            booking.setTotalAmount(BigDecimal.ZERO);

            Booking saved = bookingRepository.save(booking);
            return BookingMapper.toResponse(saved);
        }
        catch (RuntimeException exception){
            externalService.releaseSeats(request.getFlightInstanceId(), passengerCount);
            throw exception;
        }
    }

    @Override
    public BookingResponse getById(Long bookingId) {
        Booking booking = bookingHelper.findById(bookingId);

        return BookingMapper.toResponse(booking);
    }

    @Override
    public BookingResponse getByPnr(String pnr){
        Booking booking = bookingHelper.findByPnr(pnr);

        return BookingMapper.toResponse(booking);
    }

    @Override
    public Page<BookingResponse> getByUser(Long userId, Pageable pageable) {
        if(userId == null){
            throw new IllegalArgumentException("User id cannot be null");
        }

        return bookingRepository.findByUserId(userId, pageable)
                .map(BookingMapper::toResponse);
    }

    @Override
    public Page<BookingResponse> getByFlight(Long flightId, Pageable pageable) {
        if(flightId == null){
            throw new IllegalArgumentException("Flight id cannot be null");
        }

        return bookingRepository.findByFlightId(flightId, pageable)
                .map(BookingMapper::toResponse);
    }

    @Override
    public Page<BookingResponse> getByFlightInstance(Long flightInstanceId, Pageable pageable){
        if(flightInstanceId == null){
            throw new IllegalArgumentException("Flight instance id cannot be null");
        }

        return bookingRepository.findByFlightInstanceId(flightInstanceId, pageable)
                .map(BookingMapper::toResponse);
    }

    @Override
    public Page<BookingResponse> getAll(Pageable pageable) {
        return bookingRepository.findAll(pageable)
                .map(BookingMapper::toResponse);
    }

    @Override
    @Transactional
    public BookingResponse cancelBooking(Long bookingId, Long userId, String reason) {
        Booking booking = bookingHelper.findByIdAndUser(bookingId, userId);

        bookingHelper.validateCanCancel(booking);

        int passengerCount = booking.getPassengers() == null ? 0 : booking.getPassengers().size();

        booking.setBookingStatus(BookingStatus.CANCELLED);
        booking.setCancelledAt(LocalDateTime.now());
        booking.setCancellationReason(reason);

        Booking updated = bookingRepository.save(booking);

        if(passengerCount>0){
            externalService.releaseSeats(booking.getFlightInstanceId(), passengerCount);
        }

        return BookingMapper.toResponse(updated);
    }
}
