package com.baggageservice.dto;


import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaggageReportData {
    private Long id;
    private Long bookingId;
    private Long passengerId;
    private Long flightInstanceId;

    private String baggageType;

    private BigDecimal weight;
    private Integer quantity;
    private BigDecimal price;

    private String status;

    private Instant createdAt;
    private Instant updatedAt;


}
