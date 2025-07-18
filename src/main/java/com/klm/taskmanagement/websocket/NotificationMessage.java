package com.klm.taskmanagement.websocket;

/**
 * Data Transfer Object (DTO) representing a WebSocket notification message.
 *
 * @param message the content of the notification
 */
public record NotificationMessage(String message) {}