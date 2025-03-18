package ru.pavbatol.myplace.gateway.security.auth.client;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import ru.pavbatol.myplace.gateway.app.api.ApiResponse;
import ru.pavbatol.myplace.shared.dto.security.auth.AuthDtoRefreshRequest;
import ru.pavbatol.myplace.shared.dto.security.auth.AuthDtoRequest;
import ru.pavbatol.myplace.shared.dto.security.auth.AuthDtoResponse;

import java.util.UUID;

public interface SecurityAuthClient {
    ResponseEntity<ApiResponse<Void>> removeRefreshTokensByUserUuid(UUID userUuid, HttpHeaders headers);

    ResponseEntity<ApiResponse<Void>> removeAccessTokensByUserUuid(UUID userUuid, HttpHeaders headers);

    ResponseEntity<ApiResponse<Void>> clearAuthStorage(HttpHeaders headers);

    ResponseEntity<ApiResponse<AuthDtoResponse>> getNewRefreshToken(AuthDtoRefreshRequest dtoRefreshRequest, HttpHeaders headers);

    ResponseEntity<ApiResponse<String>> logoutAllSessions(HttpHeaders headers);

    ResponseEntity<ApiResponse<String>> logout(HttpHeaders headers);

    ResponseEntity<ApiResponse<AuthDtoResponse>> login(AuthDtoRequest dtoAuthRequest, HttpHeaders headers);

    ResponseEntity<ApiResponse<AuthDtoResponse>> getNewAccessToken(AuthDtoRefreshRequest dtoRefreshRequest, HttpHeaders headers);
}
