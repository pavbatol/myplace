package ru.pavbatol.myplace.gateway.security.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.pavbatol.myplace.gateway.app.api.ApiResponse;
import ru.pavbatol.myplace.gateway.app.api.ResponseHandler;
import ru.pavbatol.myplace.gateway.security.auth.client.SecurityAuthClient;
import ru.pavbatol.myplace.shared.dto.security.auth.AuthDtoRefreshRequest;
import ru.pavbatol.myplace.shared.dto.security.auth.AuthDtoRequest;
import ru.pavbatol.myplace.shared.dto.security.auth.AuthDtoResponse;

import java.util.UUID;

/**
 * Implementation of {@link SecurityAuthService} providing authentication and token management operations.
 * Acts as a service layer between controllers and the authentication client.
 *
 * <p><b>Key responsibilities:</b>
 * <ul>
 *   <li>Token management (access/refresh tokens)</li>
 *   <li>User authentication (login/logout)</li>
 *   <li>Session management (single/all sessions)</li>
 *   <li>Auth storage maintenance</li>
 * </ul>
 *
 * <p><b>Security context:</b>
 * <ul>
 *   <li>Token removal operations typically require admin privileges</li>
 *   <li>Authentication operations are public</li>
 *   <li>Session operations require valid user credentials</li>
 * </ul>
 *
 * <p>Uses {@link SecurityAuthClient} for communication with the authentication service
 * and {@link ResponseHandler} for standardized response processing.</p>
 */
@Service
@RequiredArgsConstructor
public class SecurityAuthServiceImpl implements SecurityAuthService {
    private final SecurityAuthClient client;
    private final ResponseHandler responseHandler;

    @Override
    public ApiResponse<Void> removeRefreshTokensByUserUuid(UUID userUuid, HttpHeaders headers) {
        ResponseEntity<Object> response = client.removeRefreshTokensByUserUuid(userUuid, headers);
        return responseHandler.processResponse(response, Void.class);
    }

    @Override
    public ApiResponse<Void> removeAccessTokensByUserUuid(UUID userUuid, HttpHeaders headers) {
        ResponseEntity<Object> response = client.removeAccessTokensByUserUuid(userUuid, headers);
        return responseHandler.processResponse(response, Void.class);
    }

    @Override
    public ApiResponse<Void> clearAuthStorage(HttpHeaders headers) {
        ResponseEntity<Object> response = client.clearAuthStorage(headers);
        return responseHandler.processResponse(response, Void.class);
    }

    @Override
    public ApiResponse<AuthDtoResponse> getNewRefreshToken(AuthDtoRefreshRequest dto, HttpHeaders headers) {
        ResponseEntity<Object> response = client.getNewRefreshToken(dto, headers);
        return responseHandler.processResponse(response, AuthDtoResponse.class);
    }

    @Override
    public ApiResponse<String> logoutAllSessions(HttpHeaders headers) {
        ResponseEntity<Object> response = client.logoutAllSessions(headers);
        return responseHandler.processResponse(response, String.class);
    }

    @Override
    public ApiResponse<String> logout(HttpHeaders headers) {
        ResponseEntity<Object> response = client.logout(headers);
        return responseHandler.processResponse(response, String.class);
    }

    @Override
    public ApiResponse<AuthDtoResponse> login(AuthDtoRequest dto, HttpHeaders headers) {
        ResponseEntity<Object> response = client.login(dto, headers);
        return responseHandler.processResponse(response, AuthDtoResponse.class);
    }

    @Override
    public ApiResponse<AuthDtoResponse> getNewAccessToken(AuthDtoRefreshRequest dto, HttpHeaders headers) {
        ResponseEntity<Object> response = client.getNewAccessToken(dto, headers);
        return responseHandler.processResponse(response, AuthDtoResponse.class);
    }
}
