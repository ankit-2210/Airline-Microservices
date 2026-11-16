package com.refundservice;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class RefundServiceIntegrationTest {
    @Test
    void applicationContextLoads() {
        assertNotNull(RefundServiceIntegrationTest.class);
    }

}
