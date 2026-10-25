package com.faresercive;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class FareServiceIntegrationTest {
    @Test
    void applicationContextLoads() {
        assertNotNull(FareServiceIntegrationTest.class);
    }

}
