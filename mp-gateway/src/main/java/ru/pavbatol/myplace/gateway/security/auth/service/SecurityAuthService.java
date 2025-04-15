package ru.pavbatol.myplace.gateway.security.auth.service;

import org.springframework.http.HttpHeaders;
import ru.pavbatol.myplace.gateway.app.api.ApiResponse;
import ru.pavbatol.myplace.shared.dto.security.auth.AuthDtoRefreshRequest;
import ru.pavbatol.myplace.shared.dto.security.auth.AuthDtoRequest;
import ru.pavbatol.myplace.shared.dto.security.auth.AuthDtoResponse;

import java.util.UUID;

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
