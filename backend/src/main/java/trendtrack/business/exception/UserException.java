package trendtrack.business.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UserException extends ResponseStatusException {
    private UserException(HttpStatus status, String reason) {
        super(status, reason);
    }

    public static UserException userNotFound() {
        return new UserException(HttpStatus.NOT_FOUND,
                "The specified user does not exist.");
    }

    public static UserException userAlreadyExists() {
        return new UserException(HttpStatus.CONFLICT,
                "A user with this username already exists.");
    }
}