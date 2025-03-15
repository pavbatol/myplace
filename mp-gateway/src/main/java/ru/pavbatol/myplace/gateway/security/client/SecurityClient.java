package ru.pavbatol.myplace.gateway.security.client;

import org.springframework.http.ResponseEntity;
import ru.pavbatol.myplace.gateway.app.api.ApiResponse;
import ru.pavbatol.myplace.shared.dto.security.auth.AuthDtoRefreshRequest;
import ru.pavbatol.myplace.shared.dto.security.auth.AuthDtoRequest;
import ru.pavbatol.myplace.shared.dto.security.auth.AuthDtoResponse;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

public interface SecurityClient {
    ResponseEntity<ApiResponse<Void>> removeRefreshTokensByUserUuid(UUID userUuid);

    ResponseEntity<ApiResponse<Void>> removeAccessTokensByUserUuid(UUID userUuid);

    ResponseEntity<ApiResponse<Void>> clearAuthStorage();

    ResponseEntity<ApiResponse<AuthDtoResponse>> getNewRefreshToken(HttpServletRequest request, AuthDtoRefreshRequest dtoRefreshRequest);

    ResponseEntity<ApiResponse<String>> logoutAllSessions(HttpServletRequest request);

    ResponseEntity<ApiResponse<String>> logout(HttpServletRequest request);

    ResponseEntity<ApiResponse<AuthDtoResponse>> login(HttpServletRequest request, AuthDtoRequest dtoAuthRequest);

    ResponseEntity<ApiResponse<AuthDtoResponse>> getNewAccessToken(HttpServletRequest request, AuthDtoRefreshRequest dtoRefreshRequest);
}
