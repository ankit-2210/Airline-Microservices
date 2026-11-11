package com.bookingservice.service.Impl;

import com.airlineportal.event.booking.BookingCreatedEvent;
import com.airlineportal.exception.ResourceNotFoundException;
import com.airlineportal.payload.request.Booking.BookingRequest;
import com.airlineportal.payload.request.Fare.FareQuoteRequest;
import com.airlineportal.payload.response.ApiResponse;
import com.airlineportal.payload.response.Booking.BookingResponse;
import com.airlineportal.payload.response.Fare.FareResponse;
import com.airlineportal.payload.response.Flight.FlightInstanceResponse;
import com.airlineportal.payload.response.Flight.FlightResponse;
import com.airlineportal.payload.response.Seat.SeatResponse;
import com.airlineportal.payload.response.User.UserResponse;
import com.airlineportal.utils.Booking.BookingStatus;
import com.airlineportal.utils.Booking.PaymentStatus;
import com.bookingservice.event.BookingEventPublisher;
import com.bookingservice.external.ExternalService;
import com.bookingservice.helper.BookingHelper;
import com.bookingservice.mapper.BookingEventMapper;
import com.bookingservice.mapper.BookingMapper;
import com.bookingservice.model.Booking;
import com.bookingservice.repository.BookingRepository;
import com.bookingservice.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {
    private static final Logger log = LoggerFactory.getLogger(BookingServiceImpl.class);

    private final BookingRepository bookingRepository;
    private final BookingHelper bookingHelper;
    private final ExternalService externalService;
    private final BookingEventPublisher bookingEventPublisher;

    @Override
    @Transactional
    public BookingResponse createBooking(BookingRequest request) {
        long startTime = System.currentTimeMillis();

        log.info(
                "Booking creation started: userId={}, flightInstanceId={}, passengerCount={}",
                request.getUserId(),
                request.getFlightInstanceId(),
                request.getPassengers() == null ? 0 : request.getPassengers().size()
        );

        try {
            bookingHelper.validateCreate(request);

            int passengerCount = request.getPassengers().size();

            // Verify User
            ApiResponse<UserResponse> userResponse = externalService.getUserById(request.getUserId());
            if (userResponse == null || userResponse.getData() == null) {
                log.warn("Booking creation failed: user not found, userId={}", request.getUserId());
                throw new ResourceNotFoundException("User not found with id: " + request.getUserId());
            }
            log.debug("User verified successfully: userId={}", request.getUserId());

            // Get Flight Instance
            ApiResponse<FlightInstanceResponse> instanceResponse = externalService.getInstanceById(request.getFlightInstanceId());
            if (instanceResponse == null || instanceResponse.getData() == null) {
                log.warn("Booking creation failed: flight instance not found, flightInstanceId={}", request.getFlightInstanceId());
                throw new ResourceNotFoundException("Flight instance not found with id: " + request.getFlightInstanceId());
            }

            FlightInstanceResponse instance = instanceResponse.getData();
            Long flightId = instance.getFlightId();
            if (flightId == null) {
                log.error("Booking creation failed: flight instance has no associated flight, flightInstanceId={}", request.getFlightInstanceId());
                throw new IllegalArgumentException("Flight instance is not associated with a flight");
            }

            // Verify Flight
            ApiResponse<FlightResponse> flightResponse = externalService.getFlightById(flightId);
            if (flightResponse == null || flightResponse.getData() == null) {
                log.warn("Booking creation failed: flight not found, flightId={}", flightId);
                throw new ResourceNotFoundException("Flight not found with id: " + flightId);
            }
            log.debug("Flight verified successfully: flightId={}, flightInstanceId={}", flightId, request.getFlightInstanceId());

            // Get Fare
            FareQuoteRequest fareRequest = FareQuoteRequest.builder()
                    .flightInstanceId(request.getFlightInstanceId())
                    .fareClass(request.getFareClass())
                    .passengerCount(passengerCount)
                    .build();

            ApiResponse<FareResponse> fareResponse = externalService.getFareQuote(fareRequest);
            if (fareResponse == null || fareResponse.getData() == null) {
                log.error("Booking creation failed: unable to calculate fare, flightInstanceId={}, fareClass={}", request.getFlightInstanceId(), request.getFareClass());
                throw new IllegalArgumentException("Unable to calculate fare");
            }

            FareResponse fare = fareResponse.getData();

            BigDecimal totalAmount = fare.getTotalFare();
            if (totalAmount == null || totalAmount.compareTo(BigDecimal.ZERO) <= 0) {
                log.error("Booking creation failed: invalid fare amount, flightInstanceId={}", request.getFlightInstanceId());
                throw new IllegalArgumentException("Invalid fare amount");
            }
            log.info("Fare calculated successfully: flightInstanceId={}, passengerCount={}, amount={}", request.getFlightInstanceId(), passengerCount, totalAmount);

            // Reserve Seats
            ApiResponse<Boolean> reserveResponse = externalService.reserveSeats(request.getFlightInstanceId(), passengerCount);
            if (reserveResponse == null || !Boolean.TRUE.equals(reserveResponse.getData())) {
                log.error("Booking creation failed: unable to reserve flight capacity, flightInstanceId={}, passengerCount={}", request.getFlightInstanceId(), passengerCount);
                throw new IllegalArgumentException("Unable to reserve seats");
            }
            log.info("Flight capacity reserved: flightInstanceId={}, passengerCount={}", request.getFlightInstanceId(), passengerCount);

            String pnr = bookingHelper.generateUniquePnr();

            List<String> reservedSeatNumbers = new ArrayList<>();
            try {
                Booking booking = BookingMapper.toEntity(request, pnr, flightId);

                booking.setBookingStatus(BookingStatus.PENDING);
                booking.setPaymentStatus(PaymentStatus.PENDING);

                // Actual fare from FareService
                booking.setTotalAmount(totalAmount);

                // Save first so booking ID is generated
                Booking saved = bookingRepository.save(booking);

                log.info(
                        "Booking persisted successfully: bookingId={}, pnr={}, userId={}, flightId={}",
                        saved.getId(),
                        saved.getPnr(),
                        request.getUserId(),
                        flightId
                );

                // Reserve individual seats
                for (var passenger : saved.getPassengers()) {
                    String seatNumber = passenger.getSeatNumber();
                    if (seatNumber == null || seatNumber.isBlank())
                        continue;

                    ApiResponse<SeatResponse> seatResponse = externalService.bookSeat(saved.getFlightInstanceId(), seatNumber, saved.getId());
                    if (seatResponse == null || seatResponse.getData() == null) {
                        log.error("Seat reservation failed: bookingId={}, flightInstanceId={}, seatNumber={}", saved.getId(), saved.getFlightInstanceId(), seatNumber);
                        throw new IllegalArgumentException("Unable to reserve seat: " + seatNumber);
                    }

                    String normalizedSeat = seatNumber.trim().toUpperCase();
                    reservedSeatNumbers.add(normalizedSeat);
                    log.info("Seat reserved successfully: bookingId={}, seatNumber={}", saved.getId(), normalizedSeat);
                }

                BookingCreatedEvent event = BookingEventMapper.toEvent(saved);
                bookingEventPublisher.publishBookingCreated(event);

                long duration = System.currentTimeMillis() - startTime;
                log.info(
                        "Booking created successfully: bookingId={}, pnr={}, userId={}, totalAmount={}, durationMs={}",
                        saved.getId(),
                        saved.getPnr(),
                        request.getUserId(),
                        totalAmount,
                        duration
                );

                return BookingMapper.toResponse(saved);
            }
            catch (RuntimeException exception) {
                log.error(
                        "Booking creation failed after capacity reservation: userId={}, flightInstanceId={}, reservedSeats={}, reason={}",
                        request.getUserId(),
                        request.getFlightInstanceId(),
                        reservedSeatNumbers,
                        exception.getMessage(),
                        exception
                );

                // Release individual seats
                for (String seatNumber : reservedSeatNumbers) {
                    try {
                        externalService.releaseSeat(request.getFlightInstanceId(), seatNumber);
                        log.info("Individual seat released after booking failure: flightInstanceId={}, seatNumber={}", request.getFlightInstanceId(), seatNumber);
                    }
                    catch (RuntimeException releaseException) {
                        log.error(
                                "Failed to release individual seat after booking failure: flightInstanceId={}, seatNumber={}",
                                request.getFlightInstanceId(),
                                seatNumber,
                                releaseException
                        );
                    }
                }

                // Release FlightInstance capacity
                try {
                    externalService.releaseSeats(request.getFlightInstanceId(), passengerCount);
                    log.info(
                            "Flight capacity released after booking failure: flightInstanceId={}, passengerCount={}",
                            request.getFlightInstanceId(),
                            passengerCount
                    );
                } catch (RuntimeException releaseException) {
                    log.error(
                            "Failed to release flight capacity after booking failure: flightInstanceId={}, passengerCount={}",
                            request.getFlightInstanceId(),
                            passengerCount,
                            releaseException
                    );
                }
                throw exception;
            }
        }
        catch (RuntimeException exception) {
            long duration = System.currentTimeMillis() - startTime;
            log.error(
                    "Booking creation failed: userId={}, flightInstanceId={}, durationMs={}, reason={}",
                    request.getUserId(),
                    request.getFlightInstanceId(),
                    duration,
                    exception.getMessage(),
                    exception
            );

            throw exception;
        }
    }

    @Override
    public BookingResponse getById(Long bookingId) {
        log.debug("Fetching booking by ID: bookingId={}", bookingId);
        Booking booking = bookingHelper.findById(bookingId);

        log.debug("Booking retrieved successfully: bookingId={}, pnr={}", booking.getId(), booking.getPnr());
        return BookingMapper.toResponse(booking);
    }

    @Override
    public BookingResponse getByPnr(String pnr){
        log.debug("Fetching booking by PNR: pnr={}", pnr);
        Booking booking = bookingHelper.findByPnr(pnr);

        log.debug("Booking retrieve successfully: bookingId={}, pnr={}", booking.getId(), booking.getPnr());
        return BookingMapper.toResponse(booking);
    }

    @Override
    public Page<BookingResponse> getByUser(Long userId, Pageable pageable) {
        if(userId == null){
            log.warn("Invalid booking lookup request: userId is null");
            throw new IllegalArgumentException("User id cannot be null");
        }

        log.debug("Fetching bookings for user: userId={}, page={}, size={}", userId, pageable.getPageNumber(), pageable.getPageSize());
        return bookingRepository.findByUserId(userId, pageable)
                .map(BookingMapper::toResponse);
    }

    @Override
    public Page<BookingResponse> getByFlight(Long flightId, Pageable pageable) {
        if(flightId == null){
            log.warn("Invalid booking lookup request: flightId is null");
            throw new IllegalArgumentException("Flight id cannot be null");
        }

        log.debug("Fetching bookings for flight: flightId={}, page={}, size={}", flightId, pageable.getPageNumber(), pageable.getPageSize());
        return bookingRepository.findByFlightId(flightId, pageable)
                .map(BookingMapper::toResponse);
    }

    @Override
    public Page<BookingResponse> getByFlightInstance(Long flightInstanceId, Pageable pageable){
        if(flightInstanceId == null){
            log.warn("Invalid booking lookup request: flightInstanceId is null");
            throw new IllegalArgumentException("Flight instance id cannot be null");
        }

        log.debug(
                "Fetching bookings for flight instance: flightInstanceId={}, page={}, size={}",
                flightInstanceId,
                pageable.getPageNumber(),
                pageable.getPageSize()
        );
        return bookingRepository.findByFlightInstanceId(flightInstanceId, pageable)
                .map(BookingMapper::toResponse);
    }

    @Override
    public Page<BookingResponse> getAll(Pageable pageable) {
        log.debug(
                "Fetching all bookings: page={}, size={}",
                pageable.getPageNumber(),
                pageable.getPageSize()
        );
        return bookingRepository.findAll(pageable)
                .map(BookingMapper::toResponse);
    }

    @Override
    @Transactional
    public BookingResponse cancelBooking(Long bookingId, Long userId, String reason) {
        long startTime = System.currentTimeMillis();

        log.info("Booking cancellation started: bookingId={}, userId={}", bookingId, userId);

        try {
            Booking booking = bookingHelper.findByIdAndUser(bookingId, userId);

            bookingHelper.validateCanCancel(booking);

            int passengerCount = booking.getPassengers() == null ? 0 : booking.getPassengers().size();

            booking.setBookingStatus(BookingStatus.CANCELLED);
            booking.setCancelledAt(LocalDateTime.now());
            booking.setCancellationReason(reason);

            Booking updated = bookingRepository.save(booking);

            if (passengerCount > 0) {
                for (var passenger : booking.getPassengers()) {
                    if (passenger.getSeatNumber() != null && !passenger.getSeatNumber().isBlank()) {

                        String seatNumber = passenger.getSeatNumber()
                                        .trim()
                                        .toUpperCase();
                        try {
                            externalService.releaseSeat(booking.getFlightInstanceId(), seatNumber);
                            log.info("Seat released after booking cancellation: bookingId={}, seatNumber={}", bookingId, seatNumber);
                        }
                        catch (RuntimeException exception) {
                            log.error("Failed to release seat during booking cancellation: bookingId={}, seatNumber={}", bookingId, seatNumber, exception);
                            throw exception;
                        }

                    }
                }

                try {
                    externalService.releaseSeats(booking.getFlightInstanceId(), passengerCount);
                    log.info("Flight capacity released after cancellation: bookingId={}, flightInstanceId={}, passengerCount={}", bookingId, booking.getFlightInstanceId(), passengerCount);
                }
                catch (RuntimeException exception) {
                    log.error("Failed to release flight capacity during cancellation: bookingId={}, flightInstanceId={}, passengerCount={}", bookingId, booking.getFlightInstanceId(), passengerCount, exception);
                    throw exception;
                }
            }

            long duration = System.currentTimeMillis() - startTime;
            log.info("Booking cancelled successfully: bookingId={}, userId={}, durationMs={}", bookingId, userId, duration);
            return BookingMapper.toResponse(updated);
        }
        catch (RuntimeException exception) {
            long duration = System.currentTimeMillis() - startTime;

            log.error("Booking cancellation failed: bookingId={}, userId={}, durationMs={}, reason={}", bookingId, userId, duration, exception.getMessage(), exception);
            throw exception;
        }
    }
}
