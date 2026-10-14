package com.seatsercive;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class SeatServiceIntegrationTest {
    @Test
    void applicationContextLoads() {
        assertNotNull(SeatServiceIntegrationTest.class);
    }

}
