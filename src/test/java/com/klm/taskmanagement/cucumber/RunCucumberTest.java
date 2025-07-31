package com.klm.taskmanagement.cucumber;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;


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
@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features", // Feature file path
        glue = "com.klm.taskmanagement.cucumber", // Step definitions package
        plugin = {"pretty", "summary"}
)
public class RunCucumberTest {

}