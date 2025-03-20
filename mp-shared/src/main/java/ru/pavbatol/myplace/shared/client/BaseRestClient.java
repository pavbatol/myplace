package ru.pavbatol.myplace.shared.client;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

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

    //-- Execute Custom Request
    protected <T> ResponseEntity<Object> rawExecuteRequest(@NonNull String path,
                                                           @NonNull HttpMethod method,
                                                           @Nullable UUID userUuid,
                                                           @Nullable Long userId,
                                                           @Nullable HttpHeaders initialHttpHeaders,
                                                           @Nullable T body,
                                                           @Nullable Map<String, Object> parameters) {
        return executeRequest(path, method, userUuid, userId, initialHttpHeaders, body, parameters);
    }

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

            return ResponseEntity.status(responseEntity.getStatusCode())
                    .headers(responseEntity.getHeaders())
                    .body(responseEntity.getBody());

        } catch (HttpStatusCodeException e) {
            log.error("HTTP request failed with status code: {}", e.getStatusCode());
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
}
