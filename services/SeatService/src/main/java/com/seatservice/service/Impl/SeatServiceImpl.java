package com.seatservice.service.Impl;

import com.airlineportal.exception.ResourceNotFoundException;
import com.airlineportal.payload.request.Seat.SeatCreateRequest;
import com.airlineportal.payload.response.ApiResponse;
import com.airlineportal.payload.response.Flight.FlightInstanceResponse;
import com.airlineportal.payload.response.Seat.SeatResponse;
import com.airlineportal.utils.Seat.SeatStatus;
import com.seatservice.external.ExternalService;
import com.seatservice.helper.SeatHelper;
import com.seatservice.mapper.SeatMapper;
import com.seatservice.model.Seat;
import com.seatservice.repository.SeatRepository;
import com.seatservice.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SeatServiceImpl implements SeatService {
    private final SeatRepository seatRepository;
    private final SeatHelper seatHelper;
    private final ExternalService externalService;

    @Override
    @Transactional
    public SeatResponse createSeat(SeatCreateRequest request) {
        seatHelper.validateCreate(request);

        // Get Flight Instance
        ApiResponse<FlightInstanceResponse> instanceResponse = externalService.getInstanceById(request.getFlightInstanceId());
        if (instanceResponse == null || instanceResponse.getData() == null) {
            throw new ResourceNotFoundException("Flight instance not found with id: " + request.getFlightInstanceId());
        }

        FlightInstanceResponse instance = instanceResponse.getData();

        Seat seat = SeatMapper.toEntity(request);
        Seat saved = seatRepository.save(seat);

        return SeatMapper.toResponse(saved);
    }

    @Override
    public SeatResponse getById(Long seatId) {
        Seat seat = seatHelper.findById(seatId);
        return SeatMapper.toResponse(seat);
    }

    @Override
    public SeatResponse getBySeatNumber(Long flightInstanceId, String seatNumber) {
        Seat seat = seatHelper.findByInstanceAndNumber(flightInstanceId, seatNumber);
        return SeatMapper.toResponse(seat);
    }

    @Override
    public List<SeatResponse> getByFlightInstance(Long flightInstanceId) {
        if (flightInstanceId == null) {
            throw new IllegalArgumentException("Flight instance id cannot be null");
        }

        return seatRepository.findByFlightInstanceId(flightInstanceId).stream()
                .map(SeatMapper::toResponse)
                .toList();
    }

    @Override
    public List<SeatResponse> getAvailableSeats(Long flightInstanceId) {
        if (flightInstanceId == null) {
            throw new IllegalArgumentException("Flight instance id cannot be null");
        }

        return seatRepository.findByFlightInstanceIdAndSeatStatus(flightInstanceId, SeatStatus.AVAILABLE).stream()
                .map(SeatMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public SeatResponse bookSeat(Long flightInstanceId, String seatNumber, Long bookingId) {
        if (flightInstanceId == null) {
            throw new IllegalArgumentException("Flight instance id cannot be null");
        }
        if (bookingId == null) {
            throw new IllegalArgumentException("Booking id cannot be null");
        }

        String normalizedSeat = seatHelper.normalizeSeatNumber(seatNumber);
        Seat seat = seatHelper.findByInstanceAndNumber(flightInstanceId, normalizedSeat);
        seatHelper.validateAvailable(seat);

        int updated = seatRepository.bookSeat(flightInstanceId, normalizedSeat, bookingId, SeatStatus.BOOKED, SeatStatus.AVAILABLE);
        if (updated == 0) {
            throw new IllegalStateException("Seat could not be booked: " + normalizedSeat);
        }

        Seat updatedSeat = seatHelper.findByInstanceAndNumber(flightInstanceId, normalizedSeat);
        return SeatMapper.toResponse(updatedSeat);
    }

    @Override
    @Transactional
    public SeatResponse releaseSeat(Long flightInstanceId, String seatNumber) {
        if (flightInstanceId == null) {
            throw new IllegalArgumentException("Flight instance id cannot be null");
        }

        String normalizedSeat = seatHelper.normalizeSeatNumber(seatNumber);
        Seat seat = seatHelper.findByInstanceAndNumber(flightInstanceId, normalizedSeat);

        int updated = seatRepository.releaseSeat(flightInstanceId, normalizedSeat, SeatStatus.AVAILABLE, SeatStatus.BOOKED);
        if (updated == 0) {
            throw new IllegalStateException("Seat could not be released: " + normalizedSeat);
        }

        Seat releasedSeat = seatHelper.findByInstanceAndNumber(flightInstanceId, normalizedSeat);
        return SeatMapper.toResponse(releasedSeat);
    }

    @Override
    public List<SeatResponse> getByBookingId(Long bookingId) {
        if (bookingId == null) {
            throw new IllegalArgumentException("Booking id cannot be null");
        }

        return seatRepository.findByBookingId(bookingId).stream()
                .map(SeatMapper::toResponse)
                .toList();
    }
}
