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

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.UUID;

@Component
public class SecurityUserClientImpl extends BaseRestClient implements SecurityUserClient {
    private static final String ADMIN_USER_CONTEXT = "/admin/users";

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
        return null;
    }

    @Override
    public ResponseEntity<Object> getIdByUuid(UUID userUuid, HttpHeaders headers) {
        return null;
    }

    @Override
    public ResponseEntity<Object> register(HttpServletRequest servletRequest, UserDtoRegistry dtoRegister, HttpHeaders headers) {
        return null;
    }

    @Override
    public ResponseEntity<Object> confirmRegistration(UserDtoConfirm dto, HttpHeaders headers) {
        return null;
    }
}
