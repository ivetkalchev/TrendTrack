package trendtrack.business.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class FabricException extends ResponseStatusException {
    private FabricException(HttpStatus status, String reason) {
        super(status, reason);
    }

    public static FabricException fabricDoesNotExist() {
        return new FabricException(HttpStatus.NOT_FOUND,
              "Fabric does not exist.");
    }

    public static FabricException fabricAlreadyExists() {
        return new FabricException(HttpStatus.CONFLICT,
              "Fabric already exists.");
    }

    public static FabricException insufficientStock() {
        return new FabricException(HttpStatus.CONFLICT,
                "Insufficient stock.");
    }
}