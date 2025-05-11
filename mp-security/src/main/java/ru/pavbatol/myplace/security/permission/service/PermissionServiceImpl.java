package ru.pavbatol.myplace.security.permission.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import ru.pavbatol.myplace.security.jwt.JwtProvider;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {
    private final JwtProvider jwtService;

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
     * @param requiredRoles list of role identifiers that are required to access the resource
     *                      (must not be empty, validated by caller)
     * @param bearerToken   the JWT token in "Bearer {token}" format from the Authorization header
     *
     * @throws SecurityException    if the token cannot be resolved or is invalid
     * @throws AccessDeniedException if the user doesn't possess any of the required roles
     *
     * @implNote The method performs case-sensitive role comparison. Ensure role names
     *           are consistent between the token and requirements.
     * @see JwtProvider#resolveToken(String)
     * @see JwtProvider#extractRoles(String)
     */
    @Override
    public void validateAccess(List<String> requiredRoles, String bearerToken) {
        String token = jwtService.resolveToken(bearerToken)
                .orElseThrow(() -> new SecurityException("Invalid token"));

        List<String> userRoles = jwtService.extractRoles(token);

        boolean hasAccess = requiredRoles.stream()
                .anyMatch(userRoles::contains);

        if (!hasAccess) {
            log.warn("Access denied. Required roles: {}, User roles: {}", requiredRoles, userRoles);
            throw new AccessDeniedException("Insufficient privileges");
        }

        log.debug("Access granted for roles: {}", requiredRoles);
    }
}
