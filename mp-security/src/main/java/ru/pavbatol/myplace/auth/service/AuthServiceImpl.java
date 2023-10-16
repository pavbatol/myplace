package ru.pavbatol.myplace.auth.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.pavbatol.myplace.app.exception.NotFoundException;
import ru.pavbatol.myplace.auth.dto.AuthDtoRequest;
import ru.pavbatol.myplace.auth.dto.AuthDtoResponse;
import ru.pavbatol.myplace.auth.repository.RefreshTokenRedisRepository;
import ru.pavbatol.myplace.jwt.JwtProvider;
import ru.pavbatol.myplace.user.model.User;
import ru.pavbatol.myplace.user.model.UserAuthenticatedPrincipal;
import ru.pavbatol.myplace.user.repository.UserJpaRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    public static final String KEY_SEPARATOR = " "; //Since it is impossible to register a user with login containing a space
    private static final String USER_AGENT_HEADER = "User-Agent";
    private static final String DEFAULT_USER_AGENT = "no-user-agent";
    private static final String DEFAULT_AP = "no-ap";
    private final UserJpaRepository userJpaRepository;
    private final RefreshTokenRedisRepository redisRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthDtoResponse login(HttpServletRequest servletRequest, AuthDtoRequest dtoAuthRequest) {
        final User user = getNonNullUserByLogin(dtoAuthRequest.getLogin());

        if (!checkAuthConditions(dtoAuthRequest.getPassword(), user)) {
            throw new AuthorizationServiceException(String.format("%s with login: '%s' not authorized",
                    User.class.getSimpleName(), dtoAuthRequest.getLogin()));
        }

        final String accessToken = jwtProvider.createAccessToken(user);
        final String refreshToken = jwtProvider.createRefreshToken(user);

        saveRefreshToken(servletRequest, dtoAuthRequest.getLogin(), refreshToken);

        log.debug("Access and refresh tokens are created");
        return new AuthDtoResponse(accessToken, refreshToken);
    }

    @Override
    public void logout(HttpServletRequest servletRequest) {
        if (SecurityContextHolder.getContext() != null && SecurityContextHolder.getContext().getAuthentication() != null) {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            assert principal instanceof UserAuthenticatedPrincipal
                    : "The 'principal' must be " + UserAuthenticatedPrincipal.class.getSimpleName();
            ;
            String login = ((UserAuthenticatedPrincipal) principal).getLogin();
            String composedKey = composeKey(servletRequest, login);
            redisRepository.remove(composedKey);
            log.debug("Refresh token deleted by key: {}", composedKey);
        } else {
            log.debug("The de-login was not made because the user is anonymous");
        }
        SecurityContextHolder.clearContext();
        log.debug("Context cleared in SecurityContextHolder");
    }

    @Override
    public AuthDtoResponse getNewAccessToken(HttpServletRequest servletRequest, String refreshToken) {
        return getUserByVerifyingRefreshToken(servletRequest, refreshToken)
                .map(user -> {
                    String accessToken = jwtProvider.createAccessToken(user);
                    return new AuthDtoResponse(accessToken, null);
                })
                .orElseGet(() -> new AuthDtoResponse(null, null));
    }

    @Override
    public AuthDtoResponse getNewRefreshToken(HttpServletRequest servletRequest, String refreshToken) {
        return getUserByVerifyingRefreshToken(servletRequest, refreshToken)
                .map(user -> {
                    String newRefreshToken = jwtProvider.createRefreshToken(user);
                    saveRefreshToken(servletRequest, user.getLogin(), newRefreshToken);
                    return new AuthDtoResponse(null, newRefreshToken);
                })
                .orElseGet(() -> new AuthDtoResponse(null, null));
    }

    @Override
    public void deleteRefreshTokensByUserUuid(UUID userUuid) {
        User user = getNonNullUserByUuid(userUuid);
        String login = user.getLogin();
        redisRepository.deleteAllByKeyStartWith(login + KEY_SEPARATOR);
        log.debug("All refresh tokens removed for user with uuid: {}", userUuid);
    }

    private boolean checkAuthConditions(String rawPassword, User origUser) {
        if (!passwordEncoder.matches(rawPassword, origUser.getPassword())) {
            log.debug("{} with login: {} not passed password", User.class.getSimpleName(), origUser.getLogin());
            return false;
        }
        if (origUser.getDeleted()) {
            log.debug("{} with login: {} not enabled", User.class.getSimpleName(), origUser.getLogin());
            return false;
        }
        return true;
    }

    private void saveRefreshToken(HttpServletRequest servletRequest,
                                  String login,
                                  String refreshToken) {
        String composedKey = composeKey(servletRequest, login);
        redisRepository.add(composedKey, passwordEncoder.encode(refreshToken));
    }

    private String composeKey(@NonNull HttpServletRequest servletRequest, @NonNull String key) {
        String ip = servletRequest.getRemoteAddr() != null && !servletRequest.getRemoteAddr().isEmpty()
                ? servletRequest.getRemoteAddr() : DEFAULT_AP;
        String agent = servletRequest.getHeader(USER_AGENT_HEADER) != null
                ? servletRequest.getHeader(USER_AGENT_HEADER) : DEFAULT_USER_AGENT;
        return String.format("%s%s%s%s%s", key, KEY_SEPARATOR, ip, KEY_SEPARATOR, agent);
    }

    private Optional<User> getUserByVerifyingRefreshToken(HttpServletRequest servletRequest, String refreshToken) {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final String login = jwtProvider.getRefreshTokenUsername(refreshToken);
            final String composedKey = composeKey(servletRequest, login);
            return redisRepository.find(composedKey)
                    .filter(savedToken -> passwordEncoder.matches(refreshToken, savedToken))
                    .map(savedToken -> getNonNullUserByLogin(login));
        }
        log.debug("Not verified refresh token: {}", refreshToken);
        return Optional.empty();
    }

    @NonNull
    private User getNonNullUserByLogin(String login) {
        return userJpaRepository.findByLogin(login).orElseThrow(() ->
                new NotFoundException(String.format("%s not found by %s ", User.class.getSimpleName(), login)));
    }

    @NonNull
    private User getNonNullUserByUuid(UUID uuid) {
        return userJpaRepository.findByUuid(uuid).orElseThrow(() ->
                new NotFoundException(String.format("%s not found by %s ", User.class.getSimpleName(), uuid)));
    }
}
