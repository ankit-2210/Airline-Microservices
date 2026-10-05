package com.airlineportal.payload.request.Baggage;

import com.airlineportal.utils.Baggage.BaggageStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaggageStatusUpdateRequest {
    @NotNull(message = "Status is required")
    private BaggageStatus status;

}
