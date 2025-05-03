package ru.pavbatol.myplace.gateway.security.user.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.pavbatol.myplace.shared.client.BaseRestClient;
import ru.pavbatol.myplace.shared.dto.security.user.UserDtoConfirm;
import ru.pavbatol.myplace.shared.dto.security.user.UserDtoRegistry;
import ru.pavbatol.myplace.shared.dto.security.user.UserDtoUpdatePassword;
import ru.pavbatol.myplace.shared.dto.security.user.UserDtoUpdateRoles;

import java.util.Map;
import java.util.UUID;

/**
 * REST client implementation for Security User Service API.
 * Handles user management operations including roles, authentication, and registration.
 *
 * <p>Key features:</p>
 * <ul>
 *   <li>User role management (admin context)</li>
 *   <li>User CRUD operations</li>
 *   <li>Password changes</li>
 *   <li>User registration and confirmation</li>
 * </ul>
 *
 * <p>Uses three API contexts:</p>
 * <ul>
 *   <li>{@value #ADMIN_USER_CONTEXT} - Administrative operations</li>
 *   <li>{@value #PRIVATE_USER_CONTEXT} - User-specific private operations</li>
 *   <li>{@value #PUBLIC_AUTH_CONTEXT} - Public authentication endpoints</li>
 * </ul>
 *
 * <p>Extends {@link BaseRestClient} for core REST functionality.</p>
 *
 * @see SecurityUserClient
 * @see BaseRestClient
 */
@Component
public class SecurityUserClientImpl extends BaseRestClient implements SecurityUserClient {
    private static final String ADMIN_USER_CONTEXT = "/admin/users";
    private static final String PRIVATE_USER_CONTEXT = "/users";
    private static final String PUBLIC_AUTH_CONTEXT = "/auth";

    public SecurityUserClientImpl(@Value("${app.mp.security.url}") String serverUrl,
                                  RestTemplateBuilder builder) {
        super(builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build()
        );
    }

    @Override
    public ResponseEntity<Object> updateRoles(UUID userUuid, UserDtoUpdateRoles dto, HttpHeaders headers) {
        String resourcePath = String.format("/%s/roles", userUuid);
        String fullResourcePath = ADMIN_USER_CONTEXT + resourcePath;

        return patch(fullResourcePath, headers, dto);
    }

    @Override
    public ResponseEntity<Object> delete(UUID userUuid, HttpHeaders headers) {
        String resourcePath = String.format("/%s", userUuid);
        String fullResourcePath = ADMIN_USER_CONTEXT + resourcePath;

        return delete(fullResourcePath, headers);
    }

    @Override
    public ResponseEntity<Object> findByUuid(UUID userUuid, HttpHeaders headers) {
        String resourcePath = String.format("/%s", userUuid);
        String fullResourcePath = ADMIN_USER_CONTEXT + resourcePath;

        return get(fullResourcePath, headers);
    }

    @Override
    public ResponseEntity<Object> findAll(Integer from, Integer size, HttpHeaders headers) {
        String query = "?from={from}&size={size}";
        String fullResourcePath = ADMIN_USER_CONTEXT + query;

        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );

        return get(fullResourcePath, headers, parameters);
    }

    @Override
    public ResponseEntity<Object> changePassword(UUID userUuid, UserDtoUpdatePassword dto, HttpHeaders headers) {
        String resourcePath = String.format("/%s/password", userUuid);
        String fullResourcePath = PRIVATE_USER_CONTEXT + resourcePath;

        return patch(fullResourcePath, headers, dto);
    }

    @Override
    public ResponseEntity<Object> getIdByUuid(UUID userUuid, HttpHeaders headers) {
        String resourcePath = String.format("/%s/id", userUuid);
        String fullResourcePath = PRIVATE_USER_CONTEXT + resourcePath;

        return get(fullResourcePath, headers);
    }

    @Override
    public ResponseEntity<Object> register(UserDtoRegistry dto, HttpHeaders headers) {
        String resourcePath = "/registry";
        String fullResourcePath = PUBLIC_AUTH_CONTEXT + resourcePath;

        return post(fullResourcePath, headers, dto);
    }

    @Override
    public ResponseEntity<Object> confirmRegistration(UserDtoConfirm dto, HttpHeaders headers) {
        String resourcePath = "/confirmation";
        String fullResourcePath = PUBLIC_AUTH_CONTEXT + resourcePath;

        return post(fullResourcePath, headers, dto);
    }
}
