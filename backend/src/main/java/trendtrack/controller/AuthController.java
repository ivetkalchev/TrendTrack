package trendtrack.controller;

import lombok.*;
import jakarta.validation.*;
import trendtrack.business.AuthService;
import org.springframework.http.HttpStatus;
import trendtrack.domain.user.LoginRequest;
import trendtrack.domain.user.LoginResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tokens")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        LoginResponse loginResponse = authService.login(loginRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(loginResponse);
    }
}