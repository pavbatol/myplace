package ru.pavbatol.myplace.gateway.app.access;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import ru.pavbatol.myplace.gateway.app.access.client.AccessClient;
import ru.pavbatol.myplace.gateway.app.config.webmvc.filter.CustomHeaderRequestWrapper;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static ru.pavbatol.myplace.shared.constant.HttpHeaders.*;

/**
 * Aspect for intercepting and verifying role-based access before method execution.
 *
 * <p>This aspect performs automatic security checks for methods annotated with {@code @RequiredRoles}.
 * It intercepts method calls, extracts the authorization header, and verifies access privileges
 * with the security service before allowing the method to proceed.</p>
 *
 * <p><b>Flow:</b>
 * <ol>
 *   <li>Intercepts method calls with {@code @RequiredRoles} annotation</li>
 *   <li>Extracts JWT token from Authorization header</li>
 *   <li>Validates access with {@link AccessClient}</li>
 *   <li>Proceeds with method execution only if access is granted</li>
 * </ol>
 *
 * @see RequiredRoles
 * @see AccessClient
 * @deprecated This aspect is part of the servlet stack which is being phased out in favor of WebFlux.
 * During the transition period to reactive stack, this class remains operational but will be
 * removed once the migration to WebFlux is complete.
 */
@Deprecated(since = "1.0.0.0", forRemoval = true)
@Slf4j
@Aspect
@Order(1)
@Component
@RequiredArgsConstructor
public class AccessCheckAspect {
    private final AccessClient client;

    /**
     * <p>Security aspect that performs access control checks before method execution and enhances requests with user identity.
     *
     * <p>Key functionality:
     * <ul>
     *   <li>Retrieves the current HTTP request context</li>
     *    <li>Extracts the authorization token from headers</li>
     *    <li>Verifies required roles with the security service</li>
     *   <li>Injects retrieved user identity into request headers for downstream services</li>
     *
     *   <li>Throws exceptions for any access violations</li>
     * </ul>
     *
     * @param requiredRoles the annotation containing required role definitions
     * @throws IllegalStateException if request context is unavailable or authorization header is missing
     * @throws SecurityException     if the security service denies access (wrapped HttpStatusCodeException)
     */
    @Before("@annotation(requiredRoles)")
    public void checkAccess(RequiredRoles requiredRoles) {
        log.debug("Access control triggered by {} annotation", RequiredRoles.class.getSimpleName());

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes == null) {
            throw new IllegalStateException("Request context not found");
        }
        HttpServletRequest request = attributes.getRequest();

        List<String> roles = List.of(requiredRoles.roles());
        if (roles.isEmpty()) {
            throw new IllegalArgumentException("Roles list cannot be empty");
        }

        String authToken = request.getHeader(AUTHORIZATION);
        if (authToken == null || authToken.isBlank()) {
            throw new IllegalStateException(AUTHORIZATION + " header is missing");
        }

        String userAgent = request.getHeader(USER_AGENT);

        try {
            ResponseEntity<Void> response = client.checkAccess(List.of(requiredRoles.roles()), authToken, userAgent);

            String userId = response.getHeaders().getFirst(X_USER_ID);
            String userUuid = response.getHeaders().getFirst(X_USER_UUID);

            injectHeaders(userId, userUuid, request);
        } catch (HttpStatusCodeException e) {
            String accessDenied = "Access denied for roles: " + Arrays.toString(requiredRoles.roles());
            log.error(accessDenied);
            throw new SecurityException(accessDenied, e);
        }

        log.debug("Access granted");
    }

    private void injectHeaders(String userId, String userUuid, HttpServletRequest request) {
        if (userId == null && userUuid == null) {
            log.debug("No headers to inject");
            return;
        }

        if (!(request instanceof CustomHeaderRequestWrapper)) {
            log.debug("Request is not wrapped, skipping header injection");
            return;
        }

        if (userId != null) {
            ((CustomHeaderRequestWrapper) request).injectHeader(X_USER_ID, userId);
            log.debug("Injected header {}: {}", X_USER_ID, request.getHeader(X_USER_ID));
        }

        if (userUuid != null) {
            ((CustomHeaderRequestWrapper) request).injectHeader(X_USER_UUID, userUuid);
            log.debug("Injected header {}: {}", X_USER_UUID, request.getHeader(X_USER_UUID));
        }

        log.debug("Request in aspect: {}", request.getClass().getName());
        log.debug("Headers: {}={}, {}={}",
                X_USER_ID, request.getHeader(X_USER_ID),
                X_USER_UUID, request.getHeader(X_USER_UUID));
    }
}
