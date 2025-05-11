package ru.pavbatol.myplace.gateway.app.access;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import ru.pavbatol.myplace.gateway.app.access.client.AccessClient;

import java.util.List;

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
 */
@Aspect
@Component
@RequiredArgsConstructor
public class AccessCheckAspect {
    public static final String AUTHORIZATION = "Authorization";
    private final AccessClient client;

    /**
     * Performs access control check before method execution.
     *
     * <p>This advice method:
     * <ul>
     *   <li>Retrieves the current HTTP request context</li>
     *   <li>Extracts the authorization token from headers</li>
     *   <li>Verifies required roles with the security service</li>
     *   <li>Throws exceptions for any access violations</li>
     * </ul>
     *
     * @param requiredRoles the annotation containing required role definitions
     * @throws IllegalStateException if request context is unavailable or authorization header is missing
     * @throws SecurityException if the security service denies access (wrapped HttpStatusCodeException)
     *
     */
    @Before("@annotation(requiredRoles)")
    public void checkAccess(RequiredRoles requiredRoles) {
        ServletRequestAttributes attributes = (ServletRequestAttributes)
                RequestContextHolder.getRequestAttributes();

        if (attributes == null) {
            throw new IllegalStateException("Request context not found");
        }

        String authToken = attributes.getRequest().getHeader(AUTHORIZATION);
        if (authToken == null) {
            throw new IllegalStateException("Authorization header is missing");
        }

        try {
            client.checkAccess(List.of(requiredRoles.roles()), authToken);
        } catch (HttpStatusCodeException e) {
            throw new SecurityException("Access denied", e);
        }
    }
}