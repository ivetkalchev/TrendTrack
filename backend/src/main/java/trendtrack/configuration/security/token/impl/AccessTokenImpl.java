package trendtrack.configuration.security.token.impl;

import lombok.*;
import java.util.*;
import trendtrack.configuration.security.token.AccessToken;

@Getter
@EqualsAndHashCode
public class AccessTokenImpl implements AccessToken {
    private final Long userId;
    private final Set<String> roles;

    public AccessTokenImpl(Long userId, Collection<String> roles) {
        this.userId = userId;
        this.roles = roles != null ? Set.copyOf(roles) : Collections.emptySet();
    }

    @Override
    public boolean hasRole(String roleName) {
        return this.roles.contains(roleName);
    }
}