package trendtrack.configuration.security.auth;

import lombok.*;
import java.io.IOException;
import jakarta.servlet.FilterChain;
import java.util.stream.Collectors;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;
import trendtrack.configuration.security.token.AccessToken;
import org.springframework.security.core.userdetails.UserDetails;
import trendtrack.configuration.security.token.AccessTokenDecoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import trendtrack.configuration.security.token.exception.InvalidAccessTokenException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Component
@AllArgsConstructor
public class AuthenticationRequestFilter extends OncePerRequestFilter {

    private static final String SPRING_SECURITY_ROLE_PREFIX = "ROLE_";

    private final AccessTokenDecoder accessTokenDecoder;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwt = authorizationHeader.substring(7);

            try {
                AccessToken accessToken = accessTokenDecoder.decode(jwt);
                setupSpringSecurityContext(accessToken);
            } catch (InvalidAccessTokenException e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid access token");
                response.flushBuffer();
                return;
            }
        }

        chain.doFilter(request, response);
    }

    private void setupSpringSecurityContext(AccessToken accessToken) {
        var authorities = accessToken.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(SPRING_SECURITY_ROLE_PREFIX + role))
                .collect(Collectors.toSet());

        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(String.valueOf(accessToken.getUserId()))
                .password("")
                .authorities(authorities)
                .build();

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}