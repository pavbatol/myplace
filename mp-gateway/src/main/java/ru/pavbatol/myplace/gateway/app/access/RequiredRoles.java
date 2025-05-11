package ru.pavbatol.myplace.gateway.app.access;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Method annotation that declares required roles for accessing the annotated method.
 *
 * <p>When applied to a method, triggers role-based access control through {@link AccessCheckAspect}
 * which verifies that the caller possesses at least one of the specified roles before allowing
 * method execution.
 *
 * <p><b>Usage Example:</b>
 * <pre>{@code
 * @RequiredRoles(roles = {"ADMIN", "USER"})
 * public void sensitiveOperation() {
 *     // Method implementation
 * }
 * }</pre>
 *
 * @see AccessCheckAspect
 * @since 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RequiredRoles {
    String[] roles();
}
