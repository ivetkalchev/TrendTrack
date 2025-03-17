package trendtrack.business;

import trendtrack.domain.user.*;

public interface AuthService {

    LoginResponse login(LoginRequest request);

    void validateUser(Long userId, String token);
}