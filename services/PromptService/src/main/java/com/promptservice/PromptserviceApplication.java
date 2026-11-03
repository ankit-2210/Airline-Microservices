package com.promptservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {
		"com.aiservice",
		"com.airlineportal"
})
@EnableFeignClients(basePackages = "com.airlineportal.client")
public class PromptserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PromptserviceApplication.class, args);
	}

}
