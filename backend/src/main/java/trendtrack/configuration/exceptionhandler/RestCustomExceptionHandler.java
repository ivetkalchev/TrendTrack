package trendtrack.configuration.exceptionhandler;

import java.util.*;
import java.net.URI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import trendtrack.business.exception.*;
import java.nio.file.AccessDeniedException;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.FieldError;
import org.springframework.validation.BindingResult;
import jakarta.validation.ConstraintViolationException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class RestCustomExceptionHandler extends ResponseEntityExceptionHandler {

    private static final URI VALIDATION_ERROR_TYPE = URI.create("/validation-error");

    @ExceptionHandler(UserException.class)
    public ResponseEntity<Object> handleUserException(UserException ex) {
        log.error("UserException with status {}: {}", ex.getStatusCode(), ex.getMessage(), ex);
        return buildResponseEntity(HttpStatus.valueOf(ex.getStatusCode().value()), ex.getReason(), null);
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<Object> handleAuthException(AuthException ex) {
        log.error("AuthException with status {}: {}", ex.getStatusCode(), ex.getMessage(), ex);
        return buildResponseEntity(HttpStatus.valueOf(ex.getStatusCode().value()), ex.getReason(), null);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex) {
        log.error("Access Denied: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.error("Validation error: {}", ex.getMessage(), ex);
        List<ValidationErrorDTO> errors = convertToErrorsList(ex);
        return buildResponseEntity(HttpStatus.BAD_REQUEST, "Validation failed", errors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex) {
        log.error("Constraint violation: {}", ex.getMessage(), ex);
        List<ValidationErrorDTO> errors = convertToErrorsList(ex);
        return buildResponseEntity(HttpStatus.BAD_REQUEST, "Constraint violation", errors);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Object> handleResponseStatusException(ResponseStatusException ex) {
        log.error("ResponseStatusException with status {}: {}", ex.getStatusCode(), ex.getReason(), ex);
        return buildResponseEntity(HttpStatus.valueOf(ex.getStatusCode().value()), ex.getReason(), null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception ex) {
        log.error("Unhandled exception: {}", ex.getMessage(), ex);
        return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred", null);
    }

    private ResponseEntity<Object> buildResponseEntity(HttpStatus status, String message, List<ValidationErrorDTO> errors) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(status);
        problemDetail.setDetail(message);
        problemDetail.setType(VALIDATION_ERROR_TYPE);
        if (errors != null) {
            problemDetail.setProperty("errors", errors);
        }
        return ResponseEntity.status(status).body(problemDetail);
    }

    private List<ValidationErrorDTO> convertToErrorsList(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        List<ValidationErrorDTO> errors = new ArrayList<>();
        if (bindingResult.hasErrors()) {
            for (var error : bindingResult.getAllErrors()) {
                if (error instanceof FieldError fieldError) {
                    errors.add(new ValidationErrorDTO(fieldError.getField(), fieldError.getDefaultMessage()));
                } else {
                    errors.add(new ValidationErrorDTO(error.getObjectName(), error.getDefaultMessage()));
                }
            }
        }
        return errors;
    }

    private List<ValidationErrorDTO> convertToErrorsList(ConstraintViolationException ex) {
        if (CollectionUtils.isEmpty(ex.getConstraintViolations())) {
            return Collections.emptyList();
        }
        List<ValidationErrorDTO> errors = new ArrayList<>();
        ex.getConstraintViolations().forEach(violation -> {
            String field = violation.getPropertyPath() != null ? violation.getPropertyPath().toString() : "unknown field";
            errors.add(new ValidationErrorDTO(field, violation.getMessage()));
        });
        return errors;
    }

    private record ValidationErrorDTO(String field, String error) {
    }
}