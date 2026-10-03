package com.fareservice.mapper;

import com.airlineportal.payload.request.Fare.FareCreateRequest;
import com.airlineportal.payload.response.Fare.FareResponse;
import com.fareservice.model.Fare;

import java.math.BigDecimal;

public final class FareMapper {
    private FareMapper(){

    }

    public static Fare toEntity(FareCreateRequest request) {

        return Fare.builder()
                .flightId(request.getFlightId())
                .flightInstanceId(request.getFlightInstanceId())

                .fareClass(request.getFareClass())
                .baseFare(request.getBaseFare())

                .tax(request.getTax())
                .cancellationFee(request.getCancellationFee())
                .changeFee(request.getChangeFee())

                .active(true)
                .build();
    }

    public static FareResponse toResponse(Fare fare) {

        return FareResponse.builder()
                .id(fare.getId())
                .flightId(fare.getFlightId())
                .flightInstanceId(fare.getFlightInstanceId())

                .fareClass(fare.getFareClass())
                .baseFare(fare.getBaseFare())

                .tax(fare.getTax())
                .totalFare(fare.getTotalFare())
                .cancellationFee(fare.getCancellationFee())
                .changeFee(fare.getChangeFee())

                .active(fare.isActive())

                .createdAt(fare.getCreatedAt())
                .updatedAt(fare.getUpdatedAt())
                .build();
    }

    public static FareResponse toQuoteResponse(Fare fare, BigDecimal baseFare, BigDecimal tax, BigDecimal totalFare) {

        return FareResponse.builder()
                .id(fare.getId())
                .flightId(fare.getFlightId())
                .flightInstanceId(fare.getFlightInstanceId())

                .fareClass(fare.getFareClass())
                .baseFare(baseFare)

                .tax(tax)
                .totalFare(totalFare)

                .cancellationFee(fare.getCancellationFee())
                .changeFee(fare.getChangeFee())

                .active(fare.isActive())

                .createdAt(fare.getCreatedAt())
                .updatedAt(fare.getUpdatedAt())
                .build();
    }


}
