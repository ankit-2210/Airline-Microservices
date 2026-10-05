package com.baggageservice.mapper;

import com.airlineportal.payload.request.Baggage.BaggageCreateRequest;
import com.airlineportal.payload.response.Baggage.BaggageResponse;
import com.baggageservice.model.Baggage;

import java.math.BigDecimal;

public final class BaggageMapper {
    private BaggageMapper(){

    }

    public static Baggage toEntity(BaggageCreateRequest request, Long flightInstanceId, BigDecimal price){

        return Baggage.builder()
                .bookingId(request.getBookingId())
                .passengerId(request.getPassengerId())
                .flightInstanceId(flightInstanceId)

                .baggageType(request.getBaggageType())
                .weight(request.getWeight())

                .quantity(request.getQuantity())
                .price(price)
                .build();
    }

    public static BaggageResponse toResponse(Baggage baggage) {

        return BaggageResponse.builder()
                .id(baggage.getId())
                .bookingId(baggage.getBookingId())
                .passengerId(baggage.getPassengerId())
                .flightInstanceId(baggage.getFlightInstanceId())

                .baggageType(baggage.getBaggageType())
                .weight(baggage.getWeight())

                .quantity(baggage.getQuantity())
                .price(baggage.getPrice())
                .status(baggage.getStatus())

                .createdAt(baggage.getCreatedAt())
                .updatedAt(baggage.getUpdatedAt())
                .build();
    }

}
