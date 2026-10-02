package com.paymentservice.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
public class RazorpayOrderResponse {
    private String id;

    private String entity;

    private Long amount;

    @JsonProperty("amount_paid")
    private Long amountPaid;

    @JsonProperty("amount_due")
    private Long amountDue;

    private String currency;

    private String status;

    private String receipt;

    private Integer attempts;

}
