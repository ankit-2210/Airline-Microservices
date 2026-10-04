package com.checkinservice.service.Impl;


import com.airlineportal.payload.request.CheckIn.CheckInRequest;
import com.airlineportal.payload.response.ApiResponse;
import com.airlineportal.payload.response.Booking.BookingResponse;
import com.airlineportal.payload.response.CheckIn.CheckInResponse;
import com.airlineportal.payload.response.Flight.FlightInstanceResponse;
import com.airlineportal.payload.response.Seat.SeatResponse;
import com.airlineportal.utils.CheckIn.CheckInStatus;
import com.checkinservice.external.ExternalService;
import com.checkinservice.helper.CheckInHelper;
import com.checkinservice.mapper.CheckInMapper;
import com.checkinservice.model.CheckIn;
import com.checkinservice.repository.CheckInRepository;
import com.checkinservice.service.CheckInService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CheckInServiceImpl implements CheckInService {
    private final CheckInRepository checkInRepository;
    private final CheckInHelper checkInHelper;
    private final ExternalService externalService;

    @Override
    @Transactional
    public CheckInResponse checkIn(CheckInRequest request) {

        // 1. Validate request
        checkInHelper.validateCheckIn(request);

        Long bookingId = request.getBookingId();
        Long passengerId = request.getPassengerId();

        // 2. Check duplicate check-in
        if(checkInRepository.existsByBookingIdAndPassengerId(bookingId, passengerId)){
            throw new IllegalArgumentException("Passenger is already checked in");
        }

        // 3. Get booking
        ApiResponse<BookingResponse> bookingResponse = externalService.getBookingById(bookingId);
        if(bookingResponse == null || bookingResponse.getData() == null){
            throw new IllegalArgumentException("Unable to retrieve booking");
        }
        BookingResponse booking = bookingResponse.getData();

        // 4. Validate passenger belongs to booking
        checkInHelper.validatePassenger(booking, passengerId);

        // 5. Validate booking status
        checkInHelper.validateBookingForCheckIn(booking);

        // 6. Get flight instance
        ApiResponse<FlightInstanceResponse> flightResponse = externalService.getInstanceById(booking.getFlightInstanceId());

        if(flightResponse == null || flightResponse.getData() == null){
            throw new IllegalArgumentException("Unable to retrieve flight instance");
        }
        FlightInstanceResponse flightInstance = flightResponse.getData();

        // 7. Validate flight can be checked in
        checkInHelper.validateFlightForCheckIn(flightInstance);

        // 8. Get passenger seat
        String seatNumber = checkInHelper.getPassengerSeat(booking, passengerId);

        // 9. Validate seat
        ApiResponse<SeatResponse> seatResponse = externalService.getSeat(booking.getFlightInstanceId(), seatNumber);
        if(seatResponse == null || seatResponse.getData() == null){
            throw new IllegalArgumentException("Seat not found: " + seatNumber);
        }
        SeatResponse seat = seatResponse.getData();

        checkInHelper.validateSeatForCheckIn(seat, bookingId, seatNumber);

        // 10. Create check-in
        CheckIn checkIn = CheckInMapper.toEntity(request, booking, seatNumber);
        checkIn.setStatus(CheckInStatus.CHECKED_IN);

        CheckIn savedCheckIn = checkInRepository.save(checkIn);
        return CheckInMapper.toResponse(savedCheckIn);

    }

    @Override
    public CheckInResponse getById(Long checkInId) {
        CheckIn checkIn = checkInHelper.findById(checkInId);
        return CheckInMapper.toResponse(checkIn);
    }

    @Override
    public CheckInResponse getByBookingAndPassenger(Long bookingId, Long passengerId) {
        CheckIn checkIn = checkInHelper.findByBookingAndPassenger(bookingId, passengerId);
        return CheckInMapper.toResponse(checkIn);
    }

    @Override
    public Page<CheckInResponse> getByBookingId(Long bookingId, Pageable pageable) {
        return checkInRepository.findByBookingId(bookingId, pageable)
                .map(CheckInMapper::toResponse);
    }

    @Override
    public Page<CheckInResponse> getByPassengerId(Long passengerId, Pageable pageable) {
        return checkInRepository.findByPassengerId(passengerId, pageable)
                .map(CheckInMapper::toResponse);
    }

    @Override
    public Page<CheckInResponse> getByFlightInstanceId(Long flightInstanceId, Pageable pageable) {
        return checkInRepository.findByFlightInstanceId(flightInstanceId, pageable)
                .map(CheckInMapper::toResponse);
    }

    @Override
    @Transactional
    public CheckInResponse cancelCheckIn(Long checkInId) {
        CheckIn checkIn = checkInHelper.findById(checkInId);

        checkInHelper.validateCanCancel(checkIn);
        checkIn.setStatus(CheckInStatus.CANCELLED);

        CheckIn updatedCheckIn = checkInRepository.save(checkIn);
        return CheckInMapper.toResponse(updatedCheckIn);
    }

}
