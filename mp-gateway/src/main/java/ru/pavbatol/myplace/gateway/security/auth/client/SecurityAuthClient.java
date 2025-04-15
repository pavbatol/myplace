package ru.pavbatol.myplace.gateway.security.auth.client;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import ru.pavbatol.myplace.shared.dto.security.auth.AuthDtoRefreshRequest;
import ru.pavbatol.myplace.shared.dto.security.auth.AuthDtoRequest;

import java.util.UUID;

/**
 * REST client for Security Authentication Service API.
 * Defines contract for token management and authentication operations.
 *
 * <p><b>Key responsibilities:</b>
 * <ul>
 *   <li>Token revocation (access/refresh tokens)</li>
 *   <li>User authentication (login/logout flows)</li>
 *   <li>Token generation (access/refresh)</li>
 *   <li>Auth storage cleanup</li>
 * </ul>
 *
 * <p><b>Authentication flow:</b>
 * <ul>
 *   <li>Login → Returns JWT access + refresh tokens</li>
 *   <li>Access token refresh → Via refresh token</li>
 *   <li>Logout → Revokes tokens</li>
 * </ul>
 *
 * <p><b>Security:</b>
 * <ul>
 *   <li>Uses Bearer JWT in {@link HttpHeaders}</li>
 *   <li>Refresh tokens have longer TTL than access tokens</li>
 * </ul>
 */
public interface SecurityAuthClient {
    ResponseEntity<Object> removeRefreshTokensByUserUuid(UUID userUuid, HttpHeaders headers);

    ResponseEntity<Object> removeAccessTokensByUserUuid(UUID userUuid, HttpHeaders headers);

    ResponseEntity<Object> clearAuthStorage(HttpHeaders headers);

    ResponseEntity<Object> getNewRefreshToken(AuthDtoRefreshRequest dto, HttpHeaders headers);

    ResponseEntity<Object> logoutAllSessions(HttpHeaders headers);

    ResponseEntity<Object> logout(HttpHeaders headers);

    ResponseEntity<Object> login(AuthDtoRequest dto, HttpHeaders headers);

    ResponseEntity<Object> getNewAccessToken(AuthDtoRefreshRequest dto, HttpHeaders headers);
}
