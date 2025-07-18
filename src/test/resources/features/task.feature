Feature: Task Controller Integration

  Scenario: Retrieve task by ID
    Given a task with title "Cucumber Task" exists in the database
    When I GET "/api/tasks/{id}"
    Then the response status should be 200