package com.bookingservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = BookingserviceApplication.class)
@ActiveProfiles("test")
class BookingserviceApplicationTests {

	@Test
	void contextLoads() {
	}

}
