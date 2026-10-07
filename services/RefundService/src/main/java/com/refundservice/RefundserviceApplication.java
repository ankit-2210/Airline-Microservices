package com.refundservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {
		"com.refundservice",
		"com.airlineportal"
})
@EnableFeignClients(basePackages = "com.airlineportal.client")
public class RefundserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(RefundserviceApplication.class, args);
	}

}
