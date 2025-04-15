package ru.pavbatol.myplace.gateway.security.auth.client;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import ru.pavbatol.myplace.shared.dto.security.auth.AuthDtoRefreshRequest;
import ru.pavbatol.myplace.shared.dto.security.auth.AuthDtoRequest;

import java.util.UUID;

public interface SecurityAuthClient {
    ResponseEntity<Object> removeRefreshTokensByUserUuid(UUID userUuid, HttpHeaders headers);

    ResponseEntity<Object> removeAccessTokensByUserUuid(UUID userUuid, HttpHeaders headers);

    ResponseEntity<Object> clearAuthStorage(HttpHeaders headers);

    ResponseEntity<Object> getNewRefreshToken(AuthDtoRefreshRequest dtoRefreshRequest, HttpHeaders headers);

    ResponseEntity<Object> logoutAllSessions(HttpHeaders headers);

    ResponseEntity<Object> logout(HttpHeaders headers);

    ResponseEntity<Object> login(AuthDtoRequest dtoAuthRequest, HttpHeaders headers);

    ResponseEntity<Object> getNewAccessToken(AuthDtoRefreshRequest dtoRefreshRequest, HttpHeaders headers);
}
