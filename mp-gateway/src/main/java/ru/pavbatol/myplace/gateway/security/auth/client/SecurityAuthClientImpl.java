package ru.pavbatol.myplace.gateway.security.auth.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.pavbatol.myplace.shared.client.BaseRestClient;
import ru.pavbatol.myplace.shared.dto.security.auth.AuthDtoRefreshRequest;
import ru.pavbatol.myplace.shared.dto.security.auth.AuthDtoRequest;

import java.util.UUID;

@Slf4j
@Service
public class SecurityAuthClientImpl extends BaseRestClient implements SecurityAuthClient {
    private static final String ADMIN_AUTH_CONTEXT = "/admin/auth";
    private static final String PRIVATE_AUTH_CONTEXT = "/users/auth";
    private static final String PUBLIC_AUTH_CONTEXT = "/auth";

    public SecurityAuthClientImpl(@Value("${app.mp.security.url}") String serverUrl,
                                  RestTemplateBuilder builder) {
        super(builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build());
    }

    @Override
    public ResponseEntity<Object> removeRefreshTokensByUserUuid(UUID userUuid, HttpHeaders headers) {
        String resourcePath = String.format("/users/%s/refresh-tokens", userUuid);
        String fullResourcePath = ADMIN_AUTH_CONTEXT + resourcePath;

        return delete(fullResourcePath, headers);
    }

    @Override
    public ResponseEntity<Object> removeAccessTokensByUserUuid(UUID userUuid, HttpHeaders headers) {
        String resourcePath = String.format("/users/%s/access-tokens", userUuid);
        String fullResourcePath = ADMIN_AUTH_CONTEXT + resourcePath;

        return delete(fullResourcePath, headers);
    }

    @Override
    public ResponseEntity<Object> clearAuthStorage(HttpHeaders headers) {
        String resourcePath = "/tokens";
        String fullResourcePath = ADMIN_AUTH_CONTEXT + resourcePath;

        return delete(fullResourcePath, headers);
    }

    @Override
    public ResponseEntity<Object> getNewRefreshToken(AuthDtoRefreshRequest dtoRefreshRequest, HttpHeaders headers) {
        String resourcePath = "/refresh-tokens";
        String fullResourcePath = PRIVATE_AUTH_CONTEXT + resourcePath;

        return post(fullResourcePath, headers, dtoRefreshRequest);
    }

    @Override
    public ResponseEntity<Object> logoutAllSessions(HttpHeaders headers) {
        String resourcePath = "/logout/all";
        String fullResourcePath = PRIVATE_AUTH_CONTEXT + resourcePath;

        return post(fullResourcePath, headers);
    }

    @Override
    public ResponseEntity<Object> logout(HttpHeaders headers) {
        String resourcePath = "/logout";
        String fullResourcePath = PUBLIC_AUTH_CONTEXT + resourcePath;

        return post(fullResourcePath, headers);
    }

    @Override
    public ResponseEntity<Object> login(AuthDtoRequest dtoAuthRequest, HttpHeaders headers) {
        String resourcePath = "/login";
        String fullResourcePath = PUBLIC_AUTH_CONTEXT + resourcePath;

        return post(fullResourcePath, headers, dtoAuthRequest);
    }

    @Override
    public ResponseEntity<Object> getNewAccessToken(AuthDtoRefreshRequest dtoRefreshRequest, HttpHeaders headers) {
        String resourcePath = "/tokens";
        String fullResourcePath = PUBLIC_AUTH_CONTEXT + resourcePath;

        return post(fullResourcePath, headers, dtoRefreshRequest);
    }
}
