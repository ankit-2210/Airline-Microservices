package com.paymentservice.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RazorpayPaymentLinkResponse {
    private String id;

    private String entity;

    private Long amount;

    @JsonProperty("amount_paid")
    private Long amountPaid;

    @JsonProperty("amount_due")
    private Long amountDue;

    private String currency;

    private String status;

    @JsonProperty("reference_id")
    private String referenceId;

    private String description;

    @JsonProperty("short_url")
    private String shortUrl;

    private String receipt;


}
