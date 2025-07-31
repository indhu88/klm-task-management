Feature: Task Controller Integration

  Scenario: Retrieve task by ID
    Given a user with username "testuser" and password "password" exists
    And I am an authenticated user
    And a task with title "Sample Task" exists in the database
    When I GET "/api/tasks/{id}/info"
    Then the response status should be 200