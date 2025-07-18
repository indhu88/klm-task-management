package com.klm.taskmanagement.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

/**
 * Event listener for WebSocket lifecycle events.
 * <p>
 * This component listens for key WebSocket events such as client connections,
 * topic subscriptions, and disconnections. It can be used for logging, analytics,
 * debugging, and enforcing access rules on subscriptions.
 */
@Slf4j
@Component
public class WebSocketEventListener {
    /**
     * Handles new WebSocket connection events.
     *
     * @param event the event representing a new connection to the WebSocket endpoint
     */
    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        log.info("🟢 WebSocket client connected.");
    }

    /**
     * Handles topic subscription events.
     *
     * @param event the event triggered when a client subscribes to a topic
     */
    @EventListener
    public void handleSubscriptionListener(SessionSubscribeEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String destination = accessor.getDestination();
        String sessionId = accessor.getSessionId();
        log.info("📡 WebSocket client [{}] subscribed to topic: {}", sessionId, destination);
    }

    /**
     * Handles client disconnection events.
     *
     * @param event the event triggered when a client disconnects from the WebSocket
     */
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        log.info("🔴 WebSocket client disconnected: {}", event.getSessionId());
    }
}
