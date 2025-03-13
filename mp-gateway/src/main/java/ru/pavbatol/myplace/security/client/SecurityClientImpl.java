package ru.pavbatol.myplace.security.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.pavbatol.myplace.app.api.ApiResponse;
import ru.pavbatol.myplace.app.api.ResponseHandler;
import ru.pavbatol.myplace.shared.client.BaseRestClient;
import ru.pavbatol.myplace.shared.dto.security.auth.AuthDtoRefreshRequest;
import ru.pavbatol.myplace.shared.dto.security.auth.AuthDtoResponse;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.UUID;

@Slf4j
@Component
public class SecurityClientImpl extends BaseRestClient implements SecurityClient {
    private static final String ADMIN_AUTH_CONTEXT = "/admin/auth";
    private static final String PRIVATE_AUTH_CONTEXT = "/user/auth";
    private final ResponseHandler responseHandler;

    public SecurityClientImpl(@Value("${app.mp.security.url}") String serverUrl, RestTemplateBuilder builder, ResponseHandler responseHandler) {
        super(builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build());

        this.responseHandler = responseHandler;
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

    @Override
    public ResponseEntity<ApiResponse<AuthDtoResponse>> getNewRefreshToken(HttpServletRequest request, AuthDtoRefreshRequest dtoRefreshRequest) {
        String resourcePath = "/refresh-tokens";
        String fullResourcePath = PRIVATE_AUTH_CONTEXT + resourcePath;

        ResponseEntity<Object> response;
        try {
            response = rest.exchange(
                    fullResourcePath,
                    HttpMethod.POST,
                    new HttpEntity<>(dtoRefreshRequest, extractHeaders(request)),
                    Object.class
            );
        } catch (RestClientException e) {
            throw new RuntimeException("Failed to call target service: " + e.getMessage(), e);
        }

        ApiResponse<AuthDtoResponse> apiResponse = responseHandler.processResponse(response, AuthDtoResponse.class);

        return ResponseEntity.status(response.getStatusCode()).body(apiResponse);
    }

    @Override
    public ResponseEntity<ApiResponse<String>> logoutAllSessions(HttpServletRequest request) {
        String resourcePath = "/logout/all";
        String fullResourcePath = PRIVATE_AUTH_CONTEXT + resourcePath;

        ResponseEntity<Object> response;
        try {
            response = rest.exchange(
                    fullResourcePath,
                    HttpMethod.POST,
                    new HttpEntity<>(null, extractHeaders(request)),
                    Object.class
            );
        } catch (RestClientException e) {
            throw new RuntimeException("Failed to call target service: " + e.getMessage(), e);
        }

        ApiResponse<String> apiResponse = responseHandler.processResponse(response, String.class);

        return ResponseEntity.status(response.getStatusCode()).body(apiResponse);
    }

    private HttpHeaders extractHeaders(HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        Collections.list(request.getHeaderNames())
                .forEach(headerName -> headers.set(headerName, request.getHeader(headerName)));
        return headers;
    }
}
