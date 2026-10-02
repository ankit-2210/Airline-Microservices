package com.bookingservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {
    @Bean
    public NewTopic bookingCreatedTopic(){
        return new NewTopic("booking.created.v1", 3, (short) 1);
    }

    @Bean
    public NewTopic bookingCancelledTopic(){
        return new NewTopic("booking.cancelled.v1", 3, (short) 1);
    }

}
