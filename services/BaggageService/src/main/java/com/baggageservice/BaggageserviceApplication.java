package com.baggageservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {
		"com.baggageservice",
		"com.airlineportal"
})
@EnableFeignClients(basePackages = "com.airlineportal.client")
public class BaggageserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BaggageserviceApplication.class, args);
	}

}
