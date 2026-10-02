package com.paymentservice.config;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "razorpay")
public class RazorpayConfig {
    private String keyId;

    private String keySecret;

    private String currency;

    private String baseUrl;

}
