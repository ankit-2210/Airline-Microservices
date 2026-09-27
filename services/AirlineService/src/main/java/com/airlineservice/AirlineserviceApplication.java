package com.airlineservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(scanBasePackages = {
		"com.airlineservice",
		"com.airlineportal"
})
public class AirlineserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AirlineserviceApplication.class, args);
	}

}
