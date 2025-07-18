# 📝 KLM Task Management System

A RESTful backend application built with **Java 17**, **Spring Boot 3**, and **JWT** for managing tasks, user assignments, and real-time notifications using **WebSocket**.

---

## 🚀 Features

- 🔐 JWT-based Authentication with Role Support (USER, ADMIN)
- 📋 Task CRUD with priority, due dates, status tracking (TODO → DONE)
- 💬 Task comments and user notes
- 📡 Real-time WebSocket notifications
- 🧪 Unit and integration testing with JUnit 5 & Mockito
- 🛡️ Spring Security with custom exception handling

---

## 🛠️ Tech Stack

- **Java 17**, **Spring Boot 3.x**
- **Gradle**, **H2 In-Memory DB**
- **JWT**, **Spring Security**
- **WebSocket**, **Lombok**, **Bean Validation**

---

## 🧑‍💻 Getting Started

### Prerequisites

- Java 17+
- Gradle
- Docker
### Swagger UI
Swagger UI: http://localhost:8080/swagger-ui.html
Use this to explore, test, and understand all API endpoints. JWT token-based authentication can be set using the "Authorize" button in the top-right corner of the UI.

### Clone and Run

```bash
git clone https://github.com/indhu88/klm-task-management.git
cd klm-task-management
./gradlew bootRun