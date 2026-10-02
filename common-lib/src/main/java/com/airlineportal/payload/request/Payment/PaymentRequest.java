package com.airlineportal.payload.request.Payment;

import lombok.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {

    @NotNull
    private Long bookingId;

    @NotNull
    private Long userId;

    @NotNull
    private BigDecimal amount;

    private String paymentMethod;


}
