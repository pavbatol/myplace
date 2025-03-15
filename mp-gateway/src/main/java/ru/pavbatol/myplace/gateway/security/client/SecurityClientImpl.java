package ru.pavbatol.myplace.gateway.security.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.pavbatol.myplace.gateway.app.api.ApiResponse;
import ru.pavbatol.myplace.gateway.app.api.ResponseHandler;
import ru.pavbatol.myplace.shared.client.BaseRestClient;
import ru.pavbatol.myplace.shared.dto.security.auth.AuthDtoRefreshRequest;
import ru.pavbatol.myplace.shared.dto.security.auth.AuthDtoRequest;
import ru.pavbatol.myplace.shared.dto.security.auth.AuthDtoResponse;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.UUID;

@Slf4j
@Service
public class SecurityClientImpl extends BaseRestClient implements SecurityClient {
    private static final String ADMIN_AUTH_CONTEXT = "/admin/auth";
    private static final String PRIVATE_AUTH_CONTEXT = "/user/auth";
    private static final String PUBLIC_AUTH_CONTEXT = "/auth";
    private final ResponseHandler responseHandler;

    public SecurityClientImpl(@Value("${app.mp.security.url}") String serverUrl,
                              RestTemplateBuilder builder,
                              ResponseHandler responseHandler) {
        super(builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build());

        this.responseHandler = responseHandler;
    }

    @Override
    public ResponseEntity<ApiResponse<Void>> removeRefreshTokensByUserUuid(UUID userUuid) {
        String resourcePath = String.format("/users/%s/refresh-tokens", userUuid);
        String fullResourcePath = ADMIN_AUTH_CONTEXT + resourcePath;

        ResponseEntity<Object> response = delete(fullResourcePath);
        ApiResponse<Void> apiResponse = responseHandler.processResponse(response, Void.class);

        return ResponseEntity.status(response.getStatusCode()).body(apiResponse);
    }

    @Override
    public ResponseEntity<ApiResponse<Void>> removeAccessTokensByUserUuid(UUID userUuid) {
        String resourcePath = String.format("/users/%s/access-tokens", userUuid);
        String fullResourcePath = ADMIN_AUTH_CONTEXT + resourcePath;

        ResponseEntity<Object> response = delete(fullResourcePath);
        ApiResponse<Void> apiResponse = responseHandler.processResponse(response, Void.class);

        return ResponseEntity.status(response.getStatusCode()).body(apiResponse);
    }

    @Override
    public ResponseEntity<ApiResponse<Void>> clearAuthStorage() {
        String resourcePath = "/tokens";
        String fullResourcePath = ADMIN_AUTH_CONTEXT + resourcePath;

        ResponseEntity<Object> response = delete(fullResourcePath);
        ApiResponse<Void> apiResponse = responseHandler.processResponse(response, Void.class);

        return ResponseEntity.status(response.getStatusCode()).body(apiResponse);
    }

    @Override
    public ResponseEntity<ApiResponse<AuthDtoResponse>> getNewRefreshToken(HttpServletRequest httpServletRequest,
                                                                           AuthDtoRefreshRequest dtoRefreshRequest) {
        String resourcePath = "/refresh-tokens";
        String fullResourcePath = PRIVATE_AUTH_CONTEXT + resourcePath;

        ResponseEntity<Object> response = executeRequest(fullResourcePath, HttpMethod.POST, dtoRefreshRequest, httpServletRequest);
        ApiResponse<AuthDtoResponse> apiResponse = responseHandler.processResponse(response, AuthDtoResponse.class);

        return ResponseEntity.status(response.getStatusCode()).body(apiResponse);
    }

    @Override
    public ResponseEntity<ApiResponse<String>> logoutAllSessions(HttpServletRequest httpServletRequest) {
        String resourcePath = "/logout/all";
        String fullResourcePath = PRIVATE_AUTH_CONTEXT + resourcePath;

        ResponseEntity<Object> response = executeRequest(fullResourcePath, HttpMethod.POST, null, httpServletRequest);
        ApiResponse<String> apiResponse = responseHandler.processResponse(response, String.class);

        return ResponseEntity.status(response.getStatusCode()).body(apiResponse);
    }

    @Override
    public ResponseEntity<ApiResponse<String>> logout(HttpServletRequest httpServletRequest) {
        String resourcePath = "/logout";
        String fullResourcePath = PUBLIC_AUTH_CONTEXT + resourcePath;

        ResponseEntity<Object> response = executeRequest(fullResourcePath, HttpMethod.POST, null, httpServletRequest);
        ApiResponse<String> apiResponse = responseHandler.processResponse(response, String.class);

        return ResponseEntity.status(response.getStatusCode()).body(apiResponse);
    }

    @Override
    public ResponseEntity<ApiResponse<AuthDtoResponse>> login(HttpServletRequest httpServletRequest, AuthDtoRequest dtoAuthRequest) {
        String resourcePath = "/login";
        String fullResourcePath = PUBLIC_AUTH_CONTEXT + resourcePath;

        ResponseEntity<Object> response = executeRequest(fullResourcePath, HttpMethod.POST, dtoAuthRequest, httpServletRequest);
        ApiResponse<AuthDtoResponse> apiResponse = responseHandler.processResponse(response, AuthDtoResponse.class);

        return ResponseEntity.status(response.getStatusCode()).body(apiResponse);
    }

    @Override
    public ResponseEntity<ApiResponse<AuthDtoResponse>> getNewAccessToken(HttpServletRequest httpServletRequest,
                                                                          AuthDtoRefreshRequest dtoRefreshRequest) {
        String resourcePath = "/tokens";
        String fullResourcePath = PUBLIC_AUTH_CONTEXT + resourcePath;

        ResponseEntity<Object> response = executeRequest(fullResourcePath, HttpMethod.POST, dtoRefreshRequest, httpServletRequest);
        ApiResponse<AuthDtoResponse> apiResponse = responseHandler.processResponse(response, AuthDtoResponse.class);

        return ResponseEntity.status(response.getStatusCode()).body(apiResponse);
    }

    private HttpHeaders extractHeaders(HttpServletRequest httpServletRequest) {
        HttpHeaders headers = new HttpHeaders();
        Collections.list(httpServletRequest.getHeaderNames())
                .forEach(headerName -> headers.set(headerName, httpServletRequest.getHeader(headerName)));
        return headers;
    }


    private <T> ResponseEntity<Object> executeRequest(String fullResourcePath,
                                                      HttpMethod httpMethod,
                                                      T body,
                                                      HttpServletRequest httpServletRequest) {
        try {
            return rest.exchange(
                    fullResourcePath,
                    httpMethod,
                    new HttpEntity<>(body, extractHeaders(httpServletRequest)),
                    Object.class
            );
        } catch (HttpStatusCodeException e) {
            log.error("HTTP request failed with status code: {}", e.getStatusCode(), e);
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        } catch (RestClientResponseException e) {
            log.error("RestClientResponseException occurred: {}", e.getMessage(), e);
            return ResponseEntity.status(e.getRawStatusCode()).body(e.getResponseBodyAsByteArray());
        } catch (RestClientException e) {
            log.error("RestClientException occurred: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Request processing failed. Error: " + e.getMessage());
        }
    }
}
