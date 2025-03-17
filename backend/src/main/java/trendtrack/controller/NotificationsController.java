package trendtrack.controller;

import lombok.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import trendtrack.domain.websocket.NotificationMessage;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@RestController
@AllArgsConstructor
@RequestMapping("notifications")
public class NotificationsController {
    private final SimpMessagingTemplate messagingTemplate;

    @PostMapping
    public ResponseEntity<Void> sendNotificationToUsers(@RequestBody NotificationMessage message) {
        messagingTemplate.convertAndSend("/topic/notification", message);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}