package com.paymentservice.dto.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RazorpayRefundResponse {
    private String id;
    private String entity;

    private Long amount;

    private String currency;
    private String payment_id;

    private String status;
    private String receipt;

}
