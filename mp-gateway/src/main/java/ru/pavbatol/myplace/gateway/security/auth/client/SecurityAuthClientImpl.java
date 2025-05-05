package ru.pavbatol.myplace.gateway.security.auth.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.pavbatol.myplace.shared.client.BaseRestClient;
import ru.pavbatol.myplace.shared.dto.security.auth.AuthDtoRefreshRequest;
import ru.pavbatol.myplace.shared.dto.security.auth.AuthDtoRequest;

import java.util.UUID;

/**
 * REST client implementation for Security Authentication Service API.
 * Provides operations for token management and user authentication.
 *
 * <p>Main functionality includes:</p>
 *
 * <ul>
 *   <li><b>Admin operations:</b> Token management (list/remove tokens)</li>
 *   <li><b>Private operations:</b> Obtain new refresh token, logout from all sessions</li>
 *   <li><b>Public operations:</b> Login, logout, obtain new access token</li>
 * </ul>
 *
 * <p>Handles the following token operations:</p>
 * <ul>
 *   <li>Login (obtain new tokens)</li>
 *   <li>Logout (invalidate current token)</li>
 *   <li>Remove specific tokens (admin)</li>
 *   <li>Remove all tokens (admin)</li>
 *   <li>Token refresh</li>
 * </ul>
 *
 * <p>Uses three API contexts:</p>
 * <ul>
 *   <li>{@value #ADMIN_AUTH_CONTEXT} - Administrative operations</li>
 *   <li>{@value #PRIVATE_AUTH_CONTEXT} - User-specific private operations</li>
 *   <li>{@value #PUBLIC_AUTH_CONTEXT} - Public authentication endpoints</li>
 * </ul>
 *
 * <p>Extends {@link BaseRestClient} for core REST functionality.</p>
 *
 * @see SecurityAuthClient
 */
@Component
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
