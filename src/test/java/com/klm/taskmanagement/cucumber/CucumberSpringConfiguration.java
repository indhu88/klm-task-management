package com.klm.taskmanagement.cucumber;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

/**
 * Links Cucumber with Spring Boot context for testing.
 */
@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // ✅ REQUIRED
@TestPropertySource("classpath:application-test.properties") // ✅ Add this
public class CucumberSpringConfiguration {
}
