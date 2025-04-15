package ru.pavbatol.myplace.gateway.security.role.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.pavbatol.myplace.shared.client.BaseRestClient;

@Slf4j
@Service
public class SecurityRoleClientImpl extends BaseRestClient implements SecurityRoleClient {
    private static final String ADMIN_ROLE_CONTEXT = "/admin/roles";

    public SecurityRoleClientImpl(@Value("${app.mp.security.url}") String serverUrl,
                                  RestTemplateBuilder builder) {
        super(builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build());
    }

    @Override
    public ResponseEntity<Object> findById(Long roleId, HttpHeaders headers) {
        String resourcePath = String.format("/%s", roleId);
        String fullResourcePath = ADMIN_ROLE_CONTEXT + resourcePath;

        return get(fullResourcePath, headers);
    }

    @Override
    public ResponseEntity<Object> findAll(HttpHeaders headers) {
        return get(ADMIN_ROLE_CONTEXT, headers);
    }
}
