## 🔌 WebSocket Testing

This project includes real-time WebSocket support using STOMP over SockJS. You can manually test WebSocket notifications when a task is created, updated, or deleted.

### How to Test

1. Start the Spring Boot application.
2. Open the test client:
   - Navigate to [`docs/websocket-test.html`](./docs/websocket-test.html)
   - Or open the file directly in a browser:  
     `file:///<your-project-root>/docs/websocket-test.html`
3. Click **Connect** to initiate the WebSocket connection.
4. Perform task operations (create, update, delete) via the API.
5. You will receive real-time messages under the `/topic/updates` STOMP topic.

### WebSocket Endpoint

- WebSocket URL: `ws://localhost:8080/ws`
- Topic: `/topic/updates`