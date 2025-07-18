package com.klm.taskmanagement.cucumber;


import com.klm.taskmanagement.config.TestSecurityConfig;
import org.junit.platform.suite.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;
/**
 * CucumberTestRunner is the JUnit 5 test runner for executing Cucumber feature files.
 *
 * <p>This class uses JUnit Platform Suite annotations to define a test suite that runs
 * all Cucumber scenarios defined in the feature files located under {@code src/test/resources/features}.
 *
 * <p>The {@code glue} parameter is configured to look for step definitions under the
 * {@code com.klm.taskmanagement.cucumber} package.
 *
 * <p>To run Cucumber tests, simply execute this class using your IDE or via Gradle.
 */
@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "com.klm.taskmanagement.cucumber")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = {TestSecurityConfig.class})
public class CucumberTestRunner {
}