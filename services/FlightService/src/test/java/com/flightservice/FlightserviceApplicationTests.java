package com.flightservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = FlightserviceApplication.class)
@ActiveProfiles("test")
class FlightserviceApplicationTests {

	@Test
	void contextLoads() {
	}

}
