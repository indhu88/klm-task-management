package com.klm.taskmanagement.cucumber;

import com.klm.taskmanagement.task.entity.Task;
import com.klm.taskmanagement.task.entity.TaskPriority;
import com.klm.taskmanagement.task.entity.TaskStatus;
import com.klm.taskmanagement.task.repository.TaskRepository;
import com.klm.taskmanagement.user.entity.Role;
import com.klm.taskmanagement.user.entity.User;
import com.klm.taskmanagement.user.repository.UserRepository;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Cucumber step definitions for Task management features.
 */


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration
public class TaskStepDefinitions {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TestRestTemplate restTemplate;

    private final HttpHeaders headers = new HttpHeaders();
    private ResponseEntity<String> response;
    private Long taskId;

    private String token;

    @Given("a test user exists")
    public void a_test_user_exists() {
        userRepository.findByUsername("testuser").orElseGet(() -> {
            User user = new User();
            user.setUsername("testuser");
            user.setPassword(passwordEncoder.encode("password"));
            user.setEmail("testuser@example.com");
            user.setRoles(Set.of(Role.ROLE_USER));
            return userRepository.save(user);
        });
    }
    @Given("a user with username {string} and password {string} exists")
    public void a_user_with_username_and_password_exists(String username, String password) {
        if (!userRepository.existsByUsername(username)) {
            User user = new User();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode("password"));
            user.setEmail(username + "@example.com");
            user.setRoles(Set.of(Role.ROLE_USER));

            userRepository.save(user);
        }
    }
    @Given("I am an authenticated user")
    public void i_am_authenticated_user() {
        String loginUrl = "/api/auth/login";
        String loginBody = "{\"username\": \"testuser\", \"password\": \"password\"}";

        HttpHeaders loginHeaders = new HttpHeaders();
        loginHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> loginRequest = new HttpEntity<>(loginBody, loginHeaders);

        ResponseEntity<Map> loginResponse = restTemplate.postForEntity(loginUrl, loginRequest, Map.class);

        System.out.println("Login response: " + loginResponse.getBody());

        assertEquals(HttpStatus.OK, loginResponse.getStatusCode(), "Login failed");

        // Extract the "data" map
        Map<String, Object> body = loginResponse.getBody();
        assertNotNull(body, "Login response body is null");

        Map<String, Object> data = (Map<String, Object>) body.get("data");
        assertNotNull(data, "Data field not found in login response");

        token = (String) data.get("token");
        assertNotNull(token, "Token not found in login response");

        headers.setBearerAuth(token);  // Set Authorization header
    }

    @Given("a task with title {string} exists in the database")
    public void a_task_with_title_exists_in_the_database(String title) {
        User user = userRepository.findByUsername("testuser")
                .orElseThrow(() -> new RuntimeException("User not found"));

        Task task = new Task();
        task.setTitle(title);
        task.setDescription("Sample task description");
        task.setPriority(TaskPriority.MEDIUM);
        task.setStatus(TaskStatus.TODO);
        task.setTargetDate(LocalDate.now().plusDays(7));
        task.setAssignedUser(user);

        Task savedTask = taskRepository.save(task);
        taskId = savedTask.getId();
    }

    @When("I GET {string}")
    public void i_get(String path) {
        String finalPath = path.replace("{id}", String.valueOf(taskId));

        HttpEntity<Void> request = new HttpEntity<>(headers);
        response = restTemplate.exchange(finalPath, HttpMethod.GET, request, String.class);

        System.out.println("GET " + finalPath);
        System.out.println("Response: " + response.getBody());
    }

    @Then("the response status should be {int}")
    public void the_response_status_should_be(Integer expectedStatus) {
        assertEquals(HttpStatus.valueOf(expectedStatus), response.getStatusCode());
    }
}