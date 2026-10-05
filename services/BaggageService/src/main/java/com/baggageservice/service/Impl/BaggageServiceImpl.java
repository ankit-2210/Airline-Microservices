package com.baggageservice.service.Impl;

import com.airlineportal.payload.request.Baggage.BaggageCreateRequest;
import com.airlineportal.payload.request.Baggage.BaggageStatusUpdateRequest;
import com.airlineportal.payload.response.Baggage.BaggageResponse;
import com.airlineportal.utils.Baggage.BaggageStatus;
import com.baggageservice.external.ExternalService;
import com.baggageservice.helper.BaggageHelper;
import com.baggageservice.mapper.BaggageMapper;
import com.baggageservice.model.Baggage;
import com.baggageservice.repository.BaggageRepository;
import com.baggageservice.service.BaggageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BaggageServiceImpl implements BaggageService {
    private final BaggageRepository baggageRepository;
    private final BaggageHelper baggageHelper;
    private final ExternalService externalService;

    @Override
    @Transactional
    public BaggageResponse addBaggage(BaggageCreateRequest request) {
        return null;
    }

    @Override
    public BaggageResponse getById(Long baggageId) {
        Baggage baggage = baggageHelper.findById(baggageId);
        return BaggageMapper.toResponse(baggage);
    }

    @Override
    public List<BaggageResponse> getByBookingId(Long bookingId) {
        return baggageRepository.findByBookingId(bookingId).stream()
                .map(BaggageMapper::toResponse)
                .toList();
    }

    @Override
    public List<BaggageResponse> getByPassengerId(Long passengerId) {
        return baggageRepository.findByPassengerId(passengerId).stream()
                .map(BaggageMapper::toResponse)
                .toList();
    }

    @Override
    public List<BaggageResponse> getByFlightInstanceId(Long flightInstanceId) {
        return baggageRepository.findByFlightInstanceId(flightInstanceId).stream()
                .map(BaggageMapper::toResponse)
                .toList();
    }

    @Override
    public Page<BaggageResponse> getByBookingId(Long bookingId, Pageable pageable) {
        return baggageRepository.findByBookingId(bookingId, pageable)
                .map(BaggageMapper::toResponse);
    }

    @Override
    public Page<BaggageResponse> getByPassengerId(Long passengerId, Pageable pageable) {
        return baggageRepository.findByPassengerId(passengerId, pageable)
                .map(BaggageMapper::toResponse);
    }

    @Override
    public Page<BaggageResponse> getByFlightInstanceId(Long flightInstanceId, Pageable pageable) {
        return baggageRepository.findByFlightInstanceId(flightInstanceId, pageable)
                .map(BaggageMapper::toResponse);
    }

    @Override
    @Transactional
    public BaggageResponse updateStatus(Long baggageId, BaggageStatusUpdateRequest request) {
        if(request == null || request.getStatus() == null){
            throw new IllegalArgumentException("Status is required");
        }

        Baggage baggage = baggageHelper.findById(baggageId);
        if(baggage.getStatus() == BaggageStatus.CANCELLED){
            throw new IllegalArgumentException("Cancelled baggage cannot be updated");
        }

        baggage.setStatus(request.getStatus());
        return BaggageMapper.toResponse(baggageRepository.save(baggage));
    }

    @Override
    @Transactional
    public BaggageResponse cancelBaggage(Long baggageId) {
        Baggage baggage = baggageHelper.findById(baggageId);
        if(baggage.getStatus() == BaggageStatus.CANCELLED){
            throw new IllegalArgumentException("Baggage is already cancelled");
        }
        if(baggage.getStatus() == BaggageStatus.DELIVERED){
            throw new IllegalArgumentException("Delivered baggage cannot be cancelled");
        }

        baggage.setStatus(BaggageStatus.CANCELLED);
        return BaggageMapper.toResponse(baggageRepository.save(baggage));
    }
}
