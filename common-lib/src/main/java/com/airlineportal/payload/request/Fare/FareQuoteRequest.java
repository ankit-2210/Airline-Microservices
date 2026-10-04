package com.airlineportal.payload.request.Fare;

import com.airlineportal.utils.Fare.FareClass;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FareQuoteRequest {
    @NotNull(message = "Flight instance ID is required")
    private Long flightInstanceId;

    @NotNull(message = "Fare class is required")
    private FareClass fareClass;

    @NotNull(message = "Passenger count is required")
    @Min(value = 1, message = "Passenger count must be at least 1")
    private Integer passengerCount;



}
