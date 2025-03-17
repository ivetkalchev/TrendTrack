package trendtrack.configuration.security.token;

import java.util.Set;

public interface AccessToken {

    Long getUserId();

    Set<String> getRoles();

    boolean hasRole(String roleName);
}