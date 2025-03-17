package trendtrack.business.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class OrderException extends ResponseStatusException {

    private OrderException(HttpStatus status, String reason) {
        super(status, reason);
    }

    public static OrderException invalidOrder() {
        return new OrderException(HttpStatus.BAD_REQUEST,
                "Invalid order.");
    }

    public static OrderException orderNotFound() {
        return new OrderException(HttpStatus.NOT_FOUND,
                "Order not found.");
    }
}