package com.airlineportal.payload.request.Loyalty;


import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EarnPointsRequest {
    @NotNull
    private Long userId;

    @NotNull
    private Long bookingId;

    @NotNull
    private BigDecimal bookingAmount;



}
