package com.klm.taskmanagement.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * WebSocket configuration class that enables STOMP messaging with SockJS support.
 * <p>
 * This setup allows real-time communication between server and clients via WebSocket,
 * with a fallback to SockJS for compatibility with older browsers.
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements  WebSocketMessageBrokerConfigurer {
    /**
     * Configures the message broker used for routing messages between clients and the server.
     * <p>
     * - Enables a simple in-memory broker with destination prefix "/topic".
     * - Sets application-level message prefix to "/app" for incoming messages from clients.
     *
     * @param config the MessageBrokerRegistry to configure
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic"); // Enables /topic as message channel for clients to subscribe
        config.setApplicationDestinationPrefixes("/app"); // Prefix for messages from clients to controllers
    }

    /**
     * Registers STOMP endpoint for client WebSocket connections.
     * <p>
     * Enables fallback with SockJS to support browsers that don’t support native WebSocket.
     *
     * @param registry the STOMP endpoint registry
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws") // Endpoint for WebSocket handshake
                .setAllowedOriginPatterns("*") // Allow all origins (CORS)
                .withSockJS(); // Fallback to SockJS for browsers without WebSocket
    }
}
