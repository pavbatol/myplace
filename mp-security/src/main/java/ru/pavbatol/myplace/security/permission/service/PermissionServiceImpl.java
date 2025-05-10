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
