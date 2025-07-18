package com.klm.taskmanagement.cucumber;


import com.klm.taskmanagement.config.TestSecurityConfig;
import com.klm.taskmanagement.task.entity.Task;
import com.klm.taskmanagement.task.entity.TaskPriority;
import com.klm.taskmanagement.task.entity.TaskStatus;
import com.klm.taskmanagement.task.repository.TaskRepository;
import com.klm.taskmanagement.user.entity.User;
import com.klm.taskmanagement.user.repository.UserRepository;
import io.cucumber.java.en.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Step definitions for Cucumber BDD tests related to the Task module.
 * <p>
 * This class defines how the steps written in Gherkin map to real Java methods that interact
 * with the Spring Boot application and test its REST API endpoints.
 */

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = {com.klm.taskmanagement.config.TestSecurityConfig.class})
public class TaskStepDefinitions {
    private final HttpHeaders headers = new HttpHeaders();
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    private ResponseEntity<String> response;
    @Autowired
    private UserRepository userRepository;
    private Long taskId;

    /**
     * Creates and saves a task with the given title into the in-memory database.
     * The saved task ID is stored for later use in the scenario.
     *
     * @param title the title of the task to create
     */
    @Given("a task with title {string} exists in the database")
    public void a_task_with_title_exists_in_the_database(String title) {
        // Ensure a user exists to assign
        User user = userRepository.findByUsername("testuser")
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setUsername("testuser");
                    newUser.setPassword("password"); // should be encoded in real app
                    newUser.setEmail("test@example.com");
                    return userRepository.save(newUser);
                });
        Task task = new Task();
        task.setTitle(title);
        task.setPriority(TaskPriority.MEDIUM);
        task.setStatus(TaskStatus.TODO);
        task.setDescription("Test task");
        task.setTargetDate(java.time.LocalDate.now().plusDays(5));
        task.setAssignedUser(user); // You may replace this with a valid user if needed

        Task saved = taskRepository.save(task);
        this.taskId = saved.getId();
    }

    /**
     * Sends an HTTP GET request to the specified path using TestRestTemplate.
     * If the path includes a placeholder like {id}, it will be replaced with the saved task ID.
     *
     * @param path the API path to send a GET request to (e.g., "/api/tasks/{id}")
     */

    @When("I GET {string}")
    public void i_get(String path) {
        String url = path.replace("{id}", String.valueOf(taskId));
        HttpEntity<Void> request = new HttpEntity<>(headers);
        response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
    }
    /**
     * Asserts that the HTTP response status matches the expected status code.
     *
     * @param expectedStatus the expected HTTP status code (e.g., 200)
     */
    @Then("the response status should be {int}")
    public void the_response_status_should_be(Integer expectedStatus) {
        assertEquals(HttpStatus.valueOf(expectedStatus), response.getStatusCode());
    }
    @Given("I am an authenticated user")
    public void i_am_authenticated_user() {
        String loginUrl = "/api/auth/login";
        String body = "{\"username\":\"testuser\",\"password\":\"password\"}";
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(body, headers);
        ResponseEntity<Map> loginResponse = restTemplate.postForEntity(loginUrl, request, Map.class);

        assertEquals(HttpStatus.OK, loginResponse.getStatusCode());

        String token = (String) loginResponse.getBody().get("token");
        headers.setBearerAuth(token);
    }

}
