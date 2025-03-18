package ru.pavbatol.myplace.gateway.security.auth.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.pavbatol.myplace.gateway.app.api.ApiResponse;
import ru.pavbatol.myplace.gateway.app.api.ResponseHandler;
import ru.pavbatol.myplace.shared.client.BaseRestClient;
import ru.pavbatol.myplace.shared.dto.security.auth.AuthDtoRefreshRequest;
import ru.pavbatol.myplace.shared.dto.security.auth.AuthDtoRequest;
import ru.pavbatol.myplace.shared.dto.security.auth.AuthDtoResponse;

import java.util.UUID;

@Slf4j
@Service
public class SecurityAuthClientImpl extends BaseRestClient implements SecurityAuthClient {
    private static final String ADMIN_AUTH_CONTEXT = "/admin/auth";
    private static final String PRIVATE_AUTH_CONTEXT = "/user/auth";
    private static final String PUBLIC_AUTH_CONTEXT = "/auth";
    private final ResponseHandler responseHandler;

    public SecurityAuthClientImpl(@Value("${app.mp.security.url}") String serverUrl,
                                  RestTemplateBuilder builder,
                                  ResponseHandler responseHandler) {
        super(builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build());

        this.responseHandler = responseHandler;
    }

    @Override
    public ResponseEntity<ApiResponse<Void>> removeRefreshTokensByUserUuid(UUID userUuid, HttpHeaders headers) {
        String resourcePath = String.format("/users/%s/refresh-tokens", userUuid);
        String fullResourcePath = ADMIN_AUTH_CONTEXT + resourcePath;

        ResponseEntity<Object> response = delete(fullResourcePath, headers);
        ApiResponse<Void> apiResponse = responseHandler.processResponse(response, Void.class);

        return ResponseEntity.status(response.getStatusCode()).body(apiResponse);
    }

    @Override
    public ResponseEntity<ApiResponse<Void>> removeAccessTokensByUserUuid(UUID userUuid, HttpHeaders headers) {
        String resourcePath = String.format("/users/%s/access-tokens", userUuid);
        String fullResourcePath = ADMIN_AUTH_CONTEXT + resourcePath;

        ResponseEntity<Object> response = delete(fullResourcePath, headers);
        ApiResponse<Void> apiResponse = responseHandler.processResponse(response, Void.class);

        return ResponseEntity.status(response.getStatusCode()).body(apiResponse);
    }

    @Override
    public ResponseEntity<ApiResponse<Void>> clearAuthStorage(HttpHeaders headers) {
        String resourcePath = "/tokens";
        String fullResourcePath = ADMIN_AUTH_CONTEXT + resourcePath;

        ResponseEntity<Object> response = delete(fullResourcePath, headers);
        ApiResponse<Void> apiResponse = responseHandler.processResponse(response, Void.class);

        return ResponseEntity.status(response.getStatusCode()).body(apiResponse);
    }

    @Override
    public ResponseEntity<ApiResponse<AuthDtoResponse>> getNewRefreshToken(AuthDtoRefreshRequest dtoRefreshRequest, HttpHeaders headers) {
        String resourcePath = "/refresh-tokens";
        String fullResourcePath = PRIVATE_AUTH_CONTEXT + resourcePath;

        ResponseEntity<Object> response = post(fullResourcePath, headers, dtoRefreshRequest);
        ApiResponse<AuthDtoResponse> apiResponse = responseHandler.processResponse(response, AuthDtoResponse.class);

        return ResponseEntity.status(response.getStatusCode()).body(apiResponse);
    }

    @Override
    public ResponseEntity<ApiResponse<String>> logoutAllSessions(HttpHeaders headers) {
        String resourcePath = "/logout/all";
        String fullResourcePath = PRIVATE_AUTH_CONTEXT + resourcePath;

        ResponseEntity<Object> response = post(fullResourcePath, headers);
        ApiResponse<String> apiResponse = responseHandler.processResponse(response, String.class);

        return ResponseEntity.status(response.getStatusCode()).body(apiResponse);
    }

    @Override
    public ResponseEntity<ApiResponse<String>> logout(HttpHeaders headers) {
        String resourcePath = "/logout";
        String fullResourcePath = PUBLIC_AUTH_CONTEXT + resourcePath;

        ResponseEntity<Object> response = post(fullResourcePath, headers);
        ApiResponse<String> apiResponse = responseHandler.processResponse(response, String.class);

        return ResponseEntity.status(response.getStatusCode()).body(apiResponse);
    }

    @Override
    public ResponseEntity<ApiResponse<AuthDtoResponse>> login(AuthDtoRequest dtoAuthRequest, HttpHeaders headers) {
        String resourcePath = "/login";
        String fullResourcePath = PUBLIC_AUTH_CONTEXT + resourcePath;

        ResponseEntity<Object> response = post(fullResourcePath, headers, dtoAuthRequest);
        ApiResponse<AuthDtoResponse> apiResponse = responseHandler.processResponse(response, AuthDtoResponse.class);

        return ResponseEntity.status(response.getStatusCode()).body(apiResponse);
    }

    @Override
    public ResponseEntity<ApiResponse<AuthDtoResponse>> getNewAccessToken(AuthDtoRefreshRequest dtoRefreshRequest, HttpHeaders headers) {
        String resourcePath = "/tokens";
        String fullResourcePath = PUBLIC_AUTH_CONTEXT + resourcePath;

        ResponseEntity<Object> response = post(fullResourcePath, headers, dtoRefreshRequest);
        ApiResponse<AuthDtoResponse> apiResponse = responseHandler.processResponse(response, AuthDtoResponse.class);

        return ResponseEntity.status(response.getStatusCode()).body(apiResponse);
    }
}
