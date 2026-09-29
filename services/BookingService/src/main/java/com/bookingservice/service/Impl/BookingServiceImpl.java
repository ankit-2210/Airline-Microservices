package com.bookingservice.service.Impl;

import com.airlineportal.exception.ResourceNotFoundException;
import com.airlineportal.payload.request.Booking.BookingRequest;
import com.airlineportal.payload.response.Booking.BookingResponse;
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

        externalService.getUserById(request.getUserId());
        externalService.getInstanceById(request.getFlightInstanceId());

        String pnr = bookingHelper.generateUniquePnr();
        Booking booking = BookingMapper.toEntity(request, pnr);

        booking.setBookingStatus(BookingStatus.PENDING);
        booking.setPaymentStatus(PaymentStatus.PENDING);

        /*
         * IMPORTANT:
         * Your current BookingRequest contains no fare,
         * and Flight/FlightInstance contains no price.
         *
         * Do not calculate a fake amount here.
         *
         * Replace this with FareService once you add it.
         */
        booking.setTotalAmount(BigDecimal.ZERO);

        Booking saved = bookingRepository.save(booking);
        return BookingMapper.toResponse(saved);
    }

    @Override
    public BookingResponse getById(Long bookingId) {
        Booking booking = bookingHelper.findById(bookingId);

        return BookingMapper.toResponse(booking);
    }

    @Override
    public BookingResponse getByPnr(String pnr){
        if(pnr == null || pnr.isBlank()){
            throw new IllegalArgumentException("PNR cannot be blank");
        }

        Booking booking = bookingRepository.findByPnr(pnr.trim().toUpperCase())
                        .orElseThrow(() -> new ResourceNotFoundException("Booking not found with PNR: " + pnr));
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
    public Page<BookingResponse> getAll(Pageable pageable) {
        return bookingRepository.findAll(pageable)
                .map(BookingMapper::toResponse);
    }

    @Override
    @Transactional
    public BookingResponse cancelBooking(Long bookingId, Long userId, String reason) {
        Booking booking = bookingHelper.findByIdAndUser(bookingId, userId);

        bookingHelper.validateCanCancel(booking);

        booking.setBookingStatus(BookingStatus.CANCELLED);
        booking.setCancelledAt(LocalDateTime.now());
        booking.setCancellationReason(reason);

        Booking updated = bookingRepository.save(booking);
        return BookingMapper.toResponse(updated);
    }
}
