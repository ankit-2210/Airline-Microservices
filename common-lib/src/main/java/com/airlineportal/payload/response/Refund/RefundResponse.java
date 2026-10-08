package com.airlineportal.payload.response.Refund;

import com.airlineportal.utils.Refund.RefundReason;
import com.airlineportal.utils.Refund.RefundStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefundResponse {
    private Long id;
    private Long bookingId;
    private Long userId;
    private Long paymentId;

    private String pnr;

    private BigDecimal originalAmount;
    private BigDecimal cancellationFee;
    private BigDecimal refundAmount;

    private RefundReason reason;
    private RefundStatus status;

    private String gatewayRefundId;
    private String remarks;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


}
