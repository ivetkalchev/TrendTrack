package trendtrack.configuration.security.auth;

import org.springframework.context.annotation.*;
import org.springframework.security.core.Authentication;
import trendtrack.configuration.security.token.AccessToken;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
public class RequestAuthenticatedUserProvider {

    @Bean
    @Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
    public AccessToken getAuthenticatedUserInRequest() {
        final SecurityContext context = SecurityContextHolder.getContext();
        if (context == null) {
            return null;
        }

        final Authentication authentication = context.getAuthentication();
        if (authentication == null) {
            return null;
        }

        final Object details = authentication.getDetails();
        if (!(details instanceof AccessToken)) {
            return null;
        }

        return (AccessToken) authentication.getDetails();
    }
}