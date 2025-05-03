package ru.pavbatol.myplace.gateway.security.auth.service;

import org.springframework.http.HttpHeaders;
import ru.pavbatol.myplace.gateway.app.api.ApiResponse;
import ru.pavbatol.myplace.shared.dto.security.auth.AuthDtoRefreshRequest;
import ru.pavbatol.myplace.shared.dto.security.auth.AuthDtoRequest;
import ru.pavbatol.myplace.shared.dto.security.auth.AuthDtoResponse;

import java.util.UUID;

/**
 * Service layer interface for authentication and token management operations.
 * Defines the core business logic for JWT-based security workflows.
 *
 * <p><b>Key responsibilities:</b>
 * <ul>
 *   <li>User authentication (login) and session termination (logout)</li>
 *   <li>JWT token lifecycle management (creation, refresh, revocation)</li>
 *   <li>Administrative token operations (bulk invalidation)</li>
 * </ul>
 *
 * <p><b>Security context requirements:</b>
 * <ul>
 *   <li>Public operations: {@code login()}, {@code getNewAccessToken()}</li>
 *   <li>User-private operations: {@code logout()}, {@code logoutAllSessions()}</li>
 *   <li>Admin operations: {@code remove*Tokens()}, {@code clearAuthStorage()}</li>
 * </ul>
 *
 * <p>All methods return standardized {@link ApiResponse} wrapping the result data or status.</p>
 *
 * @see SecurityAuthServiceImpl The default implementation
 * @see AuthDtoRequest Login credentials container
 * @see AuthDtoResponse Token response container
 */
public interface SecurityAuthService {
    ApiResponse<Void> removeRefreshTokensByUserUuid(UUID userUuid, HttpHeaders headers);

    ApiResponse<Void> removeAccessTokensByUserUuid(UUID userUuid, HttpHeaders headers);

    ApiResponse<Void> clearAuthStorage(HttpHeaders headers);

    ApiResponse<AuthDtoResponse> getNewRefreshToken(AuthDtoRefreshRequest dto, HttpHeaders headers);

    ApiResponse<String> logoutAllSessions(HttpHeaders headers);

    ApiResponse<String> logout(HttpHeaders headers);

    ApiResponse<AuthDtoResponse> login(AuthDtoRequest dto, HttpHeaders headers);

    ApiResponse<AuthDtoResponse> getNewAccessToken(AuthDtoRefreshRequest dto, HttpHeaders httpHeaders);
}
