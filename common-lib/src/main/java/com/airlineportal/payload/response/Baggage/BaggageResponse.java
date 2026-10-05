package com.airlineportal.payload.response.Baggage;

import com.airlineportal.utils.Baggage.BaggageStatus;
import com.airlineportal.utils.Baggage.BaggageType;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaggageResponse {
    private Long id;
    private Long bookingId;
    private Long passengerId;
    private Long flightInstanceId;

    private BaggageType baggageType;

    private BigDecimal weight;
    private Integer quantity;
    private BigDecimal price;

    private BaggageStatus status;

    private Instant createdAt;
    private Instant updatedAt;



}
