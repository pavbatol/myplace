package ru.pavbatol.myplace.auth.service;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.DeviceType;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.pavbatol.myplace.app.exception.BadRequestException;
import ru.pavbatol.myplace.app.exception.NotFoundException;
import ru.pavbatol.myplace.app.exception.RedisException;
import ru.pavbatol.myplace.auth.model.AccessTokenDetails;
import ru.pavbatol.myplace.auth.repository.AccessTokenRedisRepository;
import ru.pavbatol.myplace.auth.repository.RefreshTokenRedisRepository;
import ru.pavbatol.myplace.jwt.JwtProvider;
import ru.pavbatol.myplace.shared.dto.security.auth.AuthDtoRequest;
import ru.pavbatol.myplace.shared.dto.security.auth.AuthDtoResponse;
import ru.pavbatol.myplace.user.model.User;
import ru.pavbatol.myplace.user.model.UserAuthenticationPrincipal;
import ru.pavbatol.myplace.user.repository.UserJpaRepository;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    @Value("${app.jwt.access.storing}")
    private boolean accessTokenStoring;
    public static final String KEY_SEPARATOR = " "; //Since it is impossible to register a user with login containing a space
    private static final String USER_AGENT_HEADER = "User-Agent";
    private static final String DEFAULT_USER_AGENT = "no-user-agent";
    private static final String DEFAULT_IP = "no-ip";
    private final UserJpaRepository userJpaRepository;
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;
    private final AccessTokenRedisRepository accessTokenRedisRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    protected final RedisTemplate<String, Object> redisTemplate;

    @Override
    public AuthDtoResponse login(HttpServletRequest servletRequest, AuthDtoRequest dtoAuthRequest) {
        final User user = getNonNullUserByLogin(dtoAuthRequest.getLogin());

        if (!checkAuthConditions(dtoAuthRequest.getPassword(), user)) {
            throw new AuthorizationServiceException(String.format("%s with login: '%s' not authorized",
                    User.class.getSimpleName(), dtoAuthRequest.getLogin()));
        }

        final String accessToken = jwtProvider.createAccessToken(user);
        final String refreshToken = jwtProvider.createRefreshToken(user);
        log.debug("Access and refresh tokens are created");

        saveRefreshToken(servletRequest, dtoAuthRequest.getLogin(), refreshToken);
        log.debug("Refresh token is saved");

        if (accessTokenStoring) {
            saveAccessToken(servletRequest, dtoAuthRequest.getLogin(), accessToken);
            log.debug("Access token is saved");
        }

        return new AuthDtoResponse(accessToken, refreshToken);
    }

    @Override
    public void logout(HttpServletRequest servletRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication != null ? authentication.getPrincipal() : null;

        if (principal instanceof UserAuthenticationPrincipal) {
            String login = ((UserAuthenticationPrincipal) principal).getLogin();
            String composedKey = composeKey(servletRequest, login);

            refreshTokenRedisRepository.remove(composedKey);
            log.debug("Refresh token deleted by key: {}", composedKey);

            if (accessTokenStoring) {
                accessTokenRedisRepository.remove(composedKey);
                log.debug("Access token deleted by key: {}", composedKey);
            }
        } else {
            log.info("The de-login was not made because the user is anonymous");
        }

        SecurityContextHolder.clearContext();
        log.debug("Context cleared in SecurityContextHolder");
    }

    @Override
    public void logoutAllSessions(HttpServletRequest servletRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication != null ? authentication.getPrincipal() : null;

        if (principal instanceof UserAuthenticationPrincipal) {
            String login = ((UserAuthenticationPrincipal) principal).getLogin();
            String keyStartWith = login + KEY_SEPARATOR;

            refreshTokenRedisRepository.removeAllByKeyStartsWith(keyStartWith);
            log.debug("All refresh tokens deleted by key start with: {}", keyStartWith);

            if (accessTokenStoring) {
                accessTokenRedisRepository.removeAllByKeyStartsWith(keyStartWith);
                log.debug("All access tokens deleted by key start with: {}", keyStartWith);
            } else {
                log.debug("Access tokens that are on hand are still valid until they expire");
            }
        } else {
            throw new BadRequestException("The de-login was not made because the user is anonymous");
        }

        SecurityContextHolder.clearContext();
        log.debug("Context cleared in SecurityContextHolder");
    }

    @Override
    public AuthDtoResponse getNewAccessToken(HttpServletRequest servletRequest, String refreshToken) {
        return getUserByVerifyingRefreshToken(servletRequest, refreshToken)
                .map(user -> {
                    String accessToken = jwtProvider.createAccessToken(user);
                    if (accessTokenStoring) {
                        saveAccessToken(servletRequest, user.getLogin(), accessToken);
                    }
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

    /**
     * The method is for the admin, so it is not checked whose UUID is being used
     *
     * @param userUuid user UUID
     */
    @Override
    public void removeRefreshTokensByUserUuid(UUID userUuid) {
        User user = getNonNullUserByUuid(userUuid);
        String login = user.getLogin();
        refreshTokenRedisRepository.removeAllByKeyStartsWith(login + KEY_SEPARATOR);
        log.debug("All refresh tokens removed for user with uuid: {}", userUuid);
    }

    /**
     * The method is for the admin, so it is not checked whose UUID is being used
     *
     * @param userUuid user UUID
     */
    @Override
    public void removeAccessTokensByUserUuid(UUID userUuid) {
        if (!accessTokenStoring) {
            throw new BadRequestException("The application is running without saving access tokens. There is no need to delete the token.");
        }
        User user = getNonNullUserByUuid(userUuid);
        String login = user.getLogin();
        accessTokenRedisRepository.removeAllByKeyStartsWith(login + KEY_SEPARATOR);
        log.debug("All access tokens removed for user with uuid: {}", userUuid);
    }

    @Override
    public boolean checkAccessTokenExists(HttpServletRequest servletRequest, String accessToken) {
        log.debug("Checking access token exists");
        if (accessToken == null) {
            return false;
        }

        String login = jwtProvider.getAccessClaims(accessToken).getSubject();
        String composedKey = composeKey(servletRequest, login);

        Optional<AccessTokenDetails> accessTokenDetails = accessTokenRedisRepository.find(composedKey);

        return accessTokenDetails.isPresent() && accessToken.equals(accessTokenDetails.get().getToken());
    }

    @Override
    public void clearAuthStorage() {
        RedisConnectionFactory connectionFactory = redisTemplate.getConnectionFactory();
        if (connectionFactory != null) {
            connectionFactory.getConnection().flushAll();
        } else {
            throw new RedisException("Storage cleanup error.", "Not received RedisConnectionFactory.");
        }
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
        refreshTokenRedisRepository.set(composedKey, passwordEncoder.encode(refreshToken));
    }

    private void saveAccessToken(HttpServletRequest servletRequest, String login, String accessToken) {
        String composedKey = composeKey(servletRequest, login);

        String ip = servletRequest.getRemoteAddr() != null && !servletRequest.getRemoteAddr().isEmpty()
                ? servletRequest.getRemoteAddr() : DEFAULT_IP;

        UserAgent parsedUserAgent = UserAgent.parseUserAgentString(servletRequest.getHeader(USER_AGENT_HEADER));
        OperatingSystem operatingSystem = parsedUserAgent.getOperatingSystem();
        Browser browser = parsedUserAgent.getBrowser();
        DeviceType deviceType = operatingSystem.getDeviceType();

        String osName = operatingSystem.getName();
        String browserName = browser.getName();
        String deviceTypeName = deviceType.getName();

        AccessTokenDetails accessTokenDetails = new AccessTokenDetails(accessToken, ip, osName, browserName, deviceTypeName);

        accessTokenRedisRepository.set(composedKey, accessTokenDetails);
    }

    private String composeKey(@NonNull HttpServletRequest servletRequest, @NonNull String key) {
        String agent = servletRequest.getHeader(USER_AGENT_HEADER) != null
                ? servletRequest.getHeader(USER_AGENT_HEADER) : DEFAULT_USER_AGENT;
        return String.format("%s%s%s", key, KEY_SEPARATOR, getMD5Hash(agent));
    }

    private Optional<User> getUserByVerifyingRefreshToken(HttpServletRequest servletRequest, String refreshToken) {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final String login = jwtProvider.getRefreshTokenUsername(refreshToken);
            final String composedKey = composeKey(servletRequest, login);
            return refreshTokenRedisRepository.find(composedKey)
                    .filter(savedToken -> passwordEncoder.matches(refreshToken, savedToken))
                    .map(savedToken -> getNonNullUserByLogin(login));
        }
        log.debug("Not verified refresh token: {}", refreshToken);
        return Optional.empty();
    }

    @NonNull
    private User getNonNullUserByLogin(String login) {
        return userJpaRepository.findByLogin(login).orElseThrow(() ->
                new NotFoundException(String.format("%s not found by login: %s ", User.class.getSimpleName(), login)));
    }

    @NonNull
    private User getNonNullUserByUuid(UUID uuid) {
        return userJpaRepository.findByUuid(uuid).orElseThrow(() ->
                new NotFoundException(String.format("%s not found by %s ", User.class.getSimpleName(), uuid)));
    }

    private String getMD5Hash(@NonNull String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(input.getBytes(StandardCharsets.UTF_8));
            byte[] digest = md.digest();

            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }

            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 error", e);
        }
    }
}
