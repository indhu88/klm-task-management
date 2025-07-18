package com.klm.taskmanagement.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

/**
 * WebSocket controller for broadcasting real-time notifications to subscribed clients.
 * <p>
 * Clients send messages to the `/app/notify` endpoint. The controller then broadcasts
 * the response to all subscribers of `/topic/updates`.
 */
@Controller
@RequiredArgsConstructor
public class NotificationController {

    /**
     * Receives messages from WebSocket clients at destination `/app/notify`
     * and broadcasts a notification to `/topic/updates`.
     *
     * @param message the incoming message from the client
     * @return a modified message to be sent to all subscribers
     */
    @MessageMapping("/notify")
    @SendTo("/topic/updates")
    public NotificationMessage broadcast(NotificationMessage message) {
        return new NotificationMessage("🔔 " + message.message());
    }
}