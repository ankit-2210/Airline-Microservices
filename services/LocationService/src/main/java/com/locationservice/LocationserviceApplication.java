package com.locationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {
		"com.locationservice",
		"com.airlineportal"
})
@EnableFeignClients(basePackages = "com.airlineportal.client")
public class LocationserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(LocationserviceApplication.class, args);
	}

}
