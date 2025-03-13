package ru.pavbatol.myplace.security.client;

import org.springframework.http.ResponseEntity;
import ru.pavbatol.myplace.app.api.ApiResponse;
import ru.pavbatol.myplace.shared.dto.security.auth.AuthDtoRefreshRequest;
import ru.pavbatol.myplace.shared.dto.security.auth.AuthDtoResponse;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

public interface SecurityClient {
    void removeRefreshTokensByUserUuid(UUID userUuid);

    void removeAccessTokensByUserUuid(UUID userUuid);

    void clearAuthStorage();

    ResponseEntity<ApiResponse<AuthDtoResponse>> getNewRefreshToken(HttpServletRequest request, AuthDtoRefreshRequest dtoRefreshRequest);

    ResponseEntity<ApiResponse<String>> logoutAllSessions(HttpServletRequest request);
}
