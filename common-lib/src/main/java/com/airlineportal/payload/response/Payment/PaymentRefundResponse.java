package com.airlineportal.payload.response.Payment;


import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRefundResponse {
    private Long paymentId;
    private String gatewayRefundId;

    private String status;
    private String message;


}
