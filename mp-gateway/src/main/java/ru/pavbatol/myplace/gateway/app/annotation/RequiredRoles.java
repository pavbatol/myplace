package ru.pavbatol.myplace.gateway.app.annotation;

import ru.pavbatol.myplace.gateway.app.access.AccessCheckAspect;
import ru.pavbatol.myplace.shared.constant.HttpHeaders;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Method annotation for role-based access control, requiring the caller to have at least one specified role.
 *
 * <p>Currently processed by {@link AccessCheckAspect} but will transition to {@link ru.pavbatol.myplace.gateway.app.config.webflux.filter.AccessCheckWebFilter}
 * as part of reactive stack migration. During transition, both implementations may coexist.
 *
 * <p><b>Controller Usage:</b>
 * <pre>{@code
 * @RequiredRoles(
 *     roles = {"ADMIN", "REPORT_VIEWER"},
 *     setUserIdHeader = true,
 *     userIdHeader = "X-Authenticated-User-Id"
 * )
 * @GetMapping("/reports")
 * public Flux<Report> getReports() {
 *     // Controller logic
 * }}</pre>
 *
 * @see AccessCheckAspect
 * @since 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RequiredRoles {
    /**
     * Required roles (user must have at least one)
     */
    String[] roles();

    /**
     * Whether to inject user ID from Security service into request headers
     */
    boolean setUserIdHeader() default false;

    /**
     * Whether to inject user UUID from Security service into request headers
     */
    boolean setUserUuidHeader() default false;

    /**
     * Custom header name for user ID (default: X-User-Id)
     */
    String userIdHeader() default HttpHeaders.X_USER_ID;

    /**
     * Custom header name for user UUID (default: X-User-Uuid)
     */
    String userUuidHeader() default HttpHeaders.X_USER_UUID;
}
