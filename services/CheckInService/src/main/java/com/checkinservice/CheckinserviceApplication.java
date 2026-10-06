package com.checkinservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {
		"com.checkinservice",
		"com.airlineportal"
})
@EnableFeignClients(basePackages = "com.airlineportal.client")
public class CheckinserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CheckinserviceApplication.class, args);
	}

}
