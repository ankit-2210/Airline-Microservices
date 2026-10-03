package com.paymentservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RazorpayRestConfig {

//    @Bean
//    public RestClient razorpayRestClient(RazorpayConfig razorpayConfig){
//        return RestClient.builder()
//                .baseUrl(razorpayConfig.getBaseUrl())
//                .build();
//    }

    @Bean
    public RestClient razorpayRestClient(){
        return RestClient.builder().build();
    }

}
