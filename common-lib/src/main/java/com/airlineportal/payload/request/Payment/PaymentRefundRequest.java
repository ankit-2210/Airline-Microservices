package com.airlineportal.payload.request.Payment;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRefundRequest {
    @NotNull
    private Long bookingId;

    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal amount;


}
