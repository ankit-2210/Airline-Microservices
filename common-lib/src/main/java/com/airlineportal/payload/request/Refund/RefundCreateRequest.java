package com.airlineportal.payload.request.Refund;

import com.airlineportal.utils.Refund.RefundReason;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefundCreateRequest {

    @NotNull
    private Long bookingId;

    @NotNull
    private RefundReason reason;

    private String remarks;


}
