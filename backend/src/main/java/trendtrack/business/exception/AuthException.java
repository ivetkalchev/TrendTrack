package trendtrack.business.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class AuthException extends ResponseStatusException {
    private AuthException(HttpStatus status, String reason) {
        super(status, reason);
    }

    public static AuthException invalidCredentials() {
        return new AuthException(HttpStatus.UNAUTHORIZED,
                "Invalid username or password.");
    }

    public static AuthException noAccess() {
        return new AuthException(HttpStatus.UNAUTHORIZED,
                "Not Authorised to do that!");
    }
}