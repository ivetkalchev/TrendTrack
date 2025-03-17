package trendtrack.domain.websocket;

import lombok.*;

@Data
public class NotificationMessage {
    private String id;
    private String from;
    private String to;
    private String text;
}