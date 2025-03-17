package trendtrack.business.impl;

import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import trendtrack.domain.user.*;
import lombok.RequiredArgsConstructor;
import trendtrack.business.AuthService;
import trendtrack.persistence.UserRepository;
import org.springframework.stereotype.Service;
import trendtrack.configuration.security.token.*;
import trendtrack.business.exception.AuthException;
import trendtrack.persistence.entity.user.UserEntity;
import trendtrack.configuration.security.token.AccessTokenEncoder;
import trendtrack.configuration.security.token.impl.AccessTokenImpl;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccessTokenEncoder accessTokenEncoder;
    private final AccessTokenDecoder accessTokenDecoder;
    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        UserEntity user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(AuthException::invalidCredentials);

        if (!isPasswordValid(loginRequest.getPassword(), user.getPassword())) {
            logger.warn("Failed login attempt for username: {}", loginRequest.getUsername());
            throw AuthException.invalidCredentials();
        }

        String accessToken = generateAccessToken(user);
        logger.info("User {} logged in successfully.", loginRequest.getUsername());

        return LoginResponse.builder()
                .accessToken(accessToken)
                .build();
    }

    private boolean isPasswordValid(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    private String generateAccessToken(UserEntity user) {
        Long userId = user.getId();

        List<String> roles = user.getUserRoles().stream()
                .map(userRole -> userRole.getRole().toString())
                .toList();

        return accessTokenEncoder.encode(
                new AccessTokenImpl(userId, roles)
        );
    }

    @Override
    public void validateUser(Long userId, String token) {
        AccessToken accessToken = accessTokenDecoder.decode(token);
        Long loggedInUserId = accessToken.getUserId();
        List<String> loggedInUserRoles = new ArrayList<>(accessToken.getRoles());

        boolean isAdmin = loggedInUserRoles.contains("ADMIN");

        if (!Objects.equals(userId, loggedInUserId) && !isAdmin) {
            throw AuthException.noAccess();
        }
    }
}