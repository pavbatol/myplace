package ru.pavbatol.myplace.security.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.pavbatol.myplace.app.client.BaseRestClient;

import java.util.UUID;

@Component
public class SecurityClientImpl extends BaseRestClient implements SecurityClient {
    private static final String ADMIN_AUTH_CONTEXT = "/admin/auth";

    public SecurityClientImpl(@Value("${app.mp.security.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build());
    }

    @Override
    public void removeRefreshTokensByUserUuid(UUID userUuid) {
        String resourcePath = String.format("/users/%s/refresh-tokens", userUuid);
        String fullResourcePath = ADMIN_AUTH_CONTEXT + resourcePath;
        delete(fullResourcePath);
    }

    @Override
    public void removeAccessTokensByUserUuid(UUID userUuid) {
        String resourcePath = String.format("/users/%s/access-tokens", userUuid);
        String fullResourcePath = ADMIN_AUTH_CONTEXT + resourcePath;
        delete(fullResourcePath);
    }

    @Override
    public void clearAuthStorage() {
        String resourcePath = "/tokens";
        String fullResourcePath = ADMIN_AUTH_CONTEXT + resourcePath;
        delete(fullResourcePath);
    }
}
