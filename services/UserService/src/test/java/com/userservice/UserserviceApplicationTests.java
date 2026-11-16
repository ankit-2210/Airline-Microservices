package com.userservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest(classes = UserserviceApplication.class)
@ActiveProfiles("test")
class UserserviceApplicationTests {

	@Test
	void contextLoads() {
	}

}
