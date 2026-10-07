package com.airlineportal.payload.request.Refund;

import com.airlineportal.utils.Refund.RefundStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefundStatusUpdateRequest {
    @NotNull
    private RefundStatus status;

    private String gatewayRefundId;

    private String remarks;

}
