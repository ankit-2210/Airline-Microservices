package com.airlineportal.payload.request.Fare;


import com.airlineportal.utils.Fare.FareClass;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class FareCreateRequest {
    @NotNull(message = "Flight ID is required")
    private Long flightId;

    @NotNull(message = "Flight instance ID is required")
    private Long flightInstanceId;

    @NotNull(message = "Fare class is required")
    private FareClass fareClass;

    @NotNull(message = "Base fare is required")
    @DecimalMin(value = "0.01", message = "Base fare must be greater than zero")
    private BigDecimal baseFare;

    @DecimalMin(value = "0.00", message = "Tax cannot be negative")
    private BigDecimal tax;

    @DecimalMin(value = "0.00", message = "Cancellation fee cannot be negative")
    private BigDecimal cancellationFee;

    @DecimalMin(value = "0.00", message = "Change fee cannot be negative")
    private BigDecimal changeFee;


}
