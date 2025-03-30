package ru.pavbatol.myplace.auth.service;

import ru.pavbatol.myplace.shared.dto.security.auth.AuthDtoRequest;
import ru.pavbatol.myplace.shared.dto.security.auth.AuthDtoResponse;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

public interface AuthService {

    AuthDtoResponse login(HttpServletRequest httpServletRequest, AuthDtoRequest dtoAuthRequest);

    void logout(HttpServletRequest httpServletRequest);

    void logoutAllSessions(HttpServletRequest httpServletRequest);

    AuthDtoResponse getNewAccessToken(HttpServletRequest httpServletRequest, String refreshToken);

    AuthDtoResponse getNewRefreshToken(HttpServletRequest httpServletRequest, String refreshToken);

    void removeRefreshTokensByUserUuid(UUID userUuid);

    void removeAccessTokensByUserUuid(UUID userUuid);

    boolean checkAccessTokenExists(HttpServletRequest httpServletRequest, String accessToken);

    void clearAuthStorage();
}
