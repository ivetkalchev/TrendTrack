package trendtrack.business.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class CartException extends ResponseStatusException {
    public CartException(HttpStatus status, String reason) {
        super(status, reason);
    }

    public static CartException cartDoesNotExist() {
        return new CartException(HttpStatus.NOT_FOUND, "Cart does not exist.");
    }

    public static CartException invalidQuantity() {
        return new CartException(HttpStatus.BAD_REQUEST, "Quantity must be greater than zero.");
    }

    public static CartException itemNotFoundInCart() {
        return new CartException(HttpStatus.NOT_FOUND, "Item not found in cart.");
    }
}