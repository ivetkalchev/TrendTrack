package trendtrack.configuration.security.token;

public interface AccessTokenDecoder {

    AccessToken decode(String accessTokenEncoded);
}