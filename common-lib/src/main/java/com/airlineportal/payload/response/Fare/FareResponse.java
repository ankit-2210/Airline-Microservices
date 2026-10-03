package com.airlineportal.payload.response.Fare;

import com.airlineportal.utils.Fare.FareClass;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class FareResponse {
    private Long id;
    private Long flightId;
    private Long flightInstanceId;

    private FareClass fareClass;

    private BigDecimal baseFare;
    private BigDecimal tax;
    private BigDecimal totalFare;
    private BigDecimal cancellationFee;
    private BigDecimal changeFee;

    private Boolean active;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
