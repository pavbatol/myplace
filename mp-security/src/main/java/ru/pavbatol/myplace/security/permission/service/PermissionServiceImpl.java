package ru.pavbatol.myplace.security.permission.service;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import ru.pavbatol.myplace.security.app.exception.NotFoundException;
import ru.pavbatol.myplace.security.jwt.JwtProvider;
import ru.pavbatol.myplace.security.permission.dto.UserIdentityDto;
import ru.pavbatol.myplace.security.user.repository.UserJpaRepository;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {
    private final JwtProvider jwtService;
    private final UserJpaRepository userRepository;

    /**
     * Validates whether the user associated with the provided JWT token has at least one
     * of the required roles to access a resource.
     *
     * <p>The method performs the following steps:
     * <ol>
     *   <li>Resolves and validates the JWT token from the bearer token string</li>
     *   <li>Extracts the user's roles from the valid token</li>
     *   <li>Checks if any of the required roles matches the user's roles</li>
     *   <li>Throws an exception if access is denied, otherwise permits access</li>
     * </ol>
     *
     * @param requiredRoles        list of role identifiers that are required to access the resource
     *                             (must not be empty, validated by caller)
     * @param bearerToken          the JWT token in "Bearer {token}" format from the Authorization header
     * @param shouldReturnUserId   whether user ID should be returned
     * @param shouldReturnUserUuid whether user UUID should be returned
     * @return {@link UserIdentityDto}  containing ID/UUID if requested
     * @throws SecurityException     if the token cannot be resolved or is invalid
     * @throws AccessDeniedException if the user doesn't possess any of the required roles
     * @implNote The method performs case-sensitive role comparison. Ensure role names
     * are consistent between the token and requirements.
     * @see JwtProvider#resolveToken(String)
     * @see JwtProvider#extractRoles(String)
     */
    @Override
    public UserIdentityDto validateAccess(List<String> requiredRoles,
                                          String bearerToken,
                                          boolean shouldReturnUserId,
                                          boolean shouldReturnUserUuid) {
        String token = jwtService.resolveToken(bearerToken)
                .orElseThrow(() -> new SecurityException("Invalid token"));
        Claims claims = jwtService.getAccessClaims(token);

        List<String> userRoles = jwtService.extractRoles(claims);

        boolean hasAccess = requiredRoles.stream()
                .anyMatch(userRoles::contains);

        if (!hasAccess) {
            log.warn("Access denied. Required roles: {}, User roles: {}", requiredRoles, userRoles);
            throw new AccessDeniedException("Insufficient privileges");
        }

        log.debug("Access granted for roles: {}", requiredRoles);

        if (shouldReturnUserId || shouldReturnUserUuid) {
            return jwtService.extractUuid(claims)
                    .map(uuidStr -> new UserIdentityDto(
                                    shouldReturnUserId ? getUserId(uuidStr) : null,
                                    shouldReturnUserUuid ? UUID.fromString(uuidStr) : null
                            )
                    )
                    .orElseThrow(() -> new NotFoundException("UUID from token is null"));
        }

        return new UserIdentityDto(null, null);
    }

    private Long getUserId(String userUuid) {
        return userRepository.getIdByUuid(UUID.fromString(userUuid))
                .orElseThrow(() -> new NotFoundException("Not found ID by user UUID: " + userUuid));
    }
}
