package ru.pavbatol.myplace.shared.client;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * A base REST client provides a base implementation for RESTful client operations.
 * This class encapsulates the logic for executing various types of HTTP requests (GET, POST, PATCH, DELETE)
 * and handles response conversion and error handling.
 *
 * <p>It uses Spring's {@link RestTemplate} for making HTTP requests and provides methods
 * for adding default headers and processing responses.</p>
 */
@Slf4j
@RequiredArgsConstructor
public class BaseRestClient {
    protected final RestTemplate rest;

    //-- GET
    protected <T> ResponseEntity<Object> get(@NonNull String path,
                                             @Nullable UUID userUuid,
                                             @Nullable Long userId,
                                             @Nullable HttpHeaders initialHttpHeaders,
                                             @Nullable T body,
                                             @Nullable Map<String, Object> parameters) {
        return executeRequest(path, HttpMethod.GET, userUuid, userId, initialHttpHeaders, body, parameters);
    }

    //-- POST

    protected ResponseEntity<Object> post(@NonNull String path,
                                          @Nullable HttpHeaders initialHttpHeaders) {
        return post(path, null, null, initialHttpHeaders, null, null);
    }

    protected <T> ResponseEntity<Object> post(@NonNull String path,
                                              @Nullable HttpHeaders initialHttpHeaders,
                                              @Nullable T body) {
        return post(path, null, null, initialHttpHeaders, body, null);
    }

    protected <T> ResponseEntity<Object> post(@NonNull String path,
                                              @Nullable UUID userUuid,
                                              @Nullable Long userId,
                                              @Nullable HttpHeaders initialHttpHeaders,
                                              @Nullable T body,
                                              @Nullable Map<String, Object> parameters) {
        return executeRequest(path, HttpMethod.POST, userUuid, userId, initialHttpHeaders, body, parameters);
    }

    //-- PATCH
    protected <T> ResponseEntity<Object> patch(@NonNull String path,
                                               @Nullable UUID userUuid,
                                               @Nullable Long userId,
                                               @Nullable HttpHeaders initialHttpHeaders,
                                               @Nullable T body,
                                               @Nullable Map<String, Object> parameters) {
        return executeRequest(path, HttpMethod.PATCH, userUuid, userId, initialHttpHeaders, body, parameters);
    }

    //-- DELETE
    protected ResponseEntity<Object> delete(String path) {
        return delete(path, null);
    }

    protected ResponseEntity<Object> delete(String path, HttpHeaders initialHttpHeaders) {
        return delete(path, null, null, initialHttpHeaders, null);
    }

    protected <T> ResponseEntity<Object> delete(@NonNull String path,
                                                @Nullable UUID userUuid,
                                                @Nullable Long userId,
                                                @Nullable HttpHeaders initialHttpHeaders,
                                                @Nullable Map<String, Object> parameters) {
        return executeRequest(path, HttpMethod.DELETE, userUuid, userId, initialHttpHeaders, null, parameters);
    }

    //-- Custom set execute
    protected <T> ResponseEntity<Object> sendRequest(@NonNull String path,
                                                     @NonNull HttpMethod method,
                                                     @Nullable UUID userUuid,
                                                     @Nullable Long userId,
                                                     @Nullable HttpHeaders initialHttpHeaders,
                                                     @Nullable T body,
                                                     @Nullable Map<String, Object> parameters) {
        return executeRequest(path, method, userUuid, userId, initialHttpHeaders, body, parameters);
    }

    /**
     * Executes an HTTP request with the given parameters.
     *
     * <p>This private method handles the actual request execution, including:
     * <ul>
     *   <li>Parameter initialization (if null)</li>
     *   <li>Header preparation with default values</li>
     *   <li>Request execution with error handling</li>
     *   <li>Response conversion</li>
     * </ul>
     * </p>
     *
     * <p>If the request fails with an HTTP status code, the method catches the exception
     * and returns a ResponseEntity with the error status and response body.</p>
     *
     * @param <T>                the type of the request body
     * @param path               the endpoint path (must not be null)
     * @param method             the HTTP method (must not be null)
     * @param userUuid           the optional user UUID for authentication/identification
     * @param userId             the optional user ID for authentication/identification
     * @param initialHttpHeaders optional initial HTTP headers to include in the request
     * @param body               the optional request body
     * @param parameters         optional request parameters
     * @return ResponseEntity containing either the successful response or error response
     * from the server
     */
    private <T> ResponseEntity<Object> executeRequest(@NonNull String path,
                                                      @NonNull HttpMethod method,
                                                      @Nullable UUID userUuid,
                                                      @Nullable Long userId,
                                                      @Nullable HttpHeaders initialHttpHeaders,
                                                      @Nullable T body,
                                                      @Nullable Map<String, Object> parameters) {
        if (parameters == null) {
            parameters = Map.of();
        }

        HttpHeaders preparedHeaders = (initialHttpHeaders == null)
                ? defaultHeaders(userUuid, userId)
                : defaultHeaders(initialHttpHeaders, userUuid, userId);

        HttpEntity<T> httpEntity = new HttpEntity<>(body, preparedHeaders);

        try {
            ResponseEntity<String> responseEntity = rest.exchange(path, method, httpEntity, String.class, parameters);
            return convertResponse(responseEntity);
        } catch (HttpStatusCodeException e) {
            log.error("HTTP request failed with status code: {}", e.getStatusCode());
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }
    }

    private HttpHeaders defaultHeaders(@Nullable UUID userUuid, @Nullable Long userId) {
        return HttpHeadersBuilder.create()
                .withUserUuid(userUuid)
                .withUserId(userId)
                .build();
    }

    private HttpHeaders defaultHeaders(HttpHeaders headers, @Nullable UUID userUuid, @Nullable Long userId) {
        return HttpHeadersBuilder.create(headers)
                .withUserUuid(userUuid)
                .withUserId(userId)
                .build();
    }

    private ResponseEntity<Object> convertResponse(ResponseEntity<?> response) {
        return ResponseEntity.status(response.getStatusCode())
                .headers(response.getHeaders())
                .body(response.getBody());
    }
}
