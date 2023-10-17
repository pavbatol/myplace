package ru.pavbatol.myplace.auth.service;

import ru.pavbatol.myplace.auth.dto.AuthDtoRequest;
import ru.pavbatol.myplace.auth.dto.AuthDtoResponse;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

public interface AuthService {

    AuthDtoResponse login(HttpServletRequest httpServletRequest, AuthDtoRequest dtoAuthRequest);

    void logout(HttpServletRequest httpServletRequest);

    AuthDtoResponse getNewAccessToken(HttpServletRequest httpServletRequest, String refreshToken);

    AuthDtoResponse getNewRefreshToken(HttpServletRequest httpServletRequest, String refreshToken);

    void deleteRefreshTokensByUserUuid(UUID userUuid);

    void deleteAccessTokensByUserUuid(UUID userUuid);
}
