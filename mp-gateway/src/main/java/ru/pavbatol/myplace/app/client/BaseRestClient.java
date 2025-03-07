package ru.pavbatol.myplace.app.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class BaseRestClient {
    private static final String X_USER_ID = "X-User-Id";
    private static final String X_USER_UUID = "X-User-Uuid";
    private final RestTemplate rest;

    // get
    protected ResponseEntity<Object> get(String path) {
        return get(path, null);
    }

    protected ResponseEntity<Object> get(String path, UUID userUuid) {
        return get(path, userUuid, null);
    }

    protected ResponseEntity<Object> get(String path, UUID userUuid, Long userId) {
        return get(path, userUuid, userId, null);
    }

    protected ResponseEntity<Object> get(String path, UUID userUuid, Long userId, @Nullable Map<String, Object> parameters) {
        return executeRestCall(HttpMethod.GET, path, userUuid, userId, parameters, null);
    }

    // post
    protected <T> ResponseEntity<Object> post(String path, T body) {
        return post(path, null, body);
    }

    protected <T> ResponseEntity<Object> post(String path, UUID userUuid, T body) {
        return post(path, userUuid, null, body);
    }

    protected <T> ResponseEntity<Object> post(String path, UUID userUuid, Long userId, T body) {
        return post(path, userUuid, userId, null, body);
    }

    protected <T> ResponseEntity<Object> post(String path, UUID userUuid, Long userId, @Nullable Map<String, Object> parameters, T body) {
        return executeRestCall(HttpMethod.GET, path, userUuid, userId, parameters, body);
    }

    // put
    protected <T> ResponseEntity<Object> put(String path, UUID userUuid, Long userId, @Nullable Map<String, Object> parameters, T body) {
        return executeRestCall(HttpMethod.PUT, path, userUuid, userId, parameters, body);
    }

    // patch
    protected <T> ResponseEntity<Object> patch(String path, T body) {
        return patch(path, null, body);
    }

    protected <T> ResponseEntity<Object> patch(String path, UUID userUuid, T body) {
        return patch(path, userUuid, null, body);
    }

    protected <T> ResponseEntity<Object> patch(String path, UUID userUuid, Long userId, T body) {
        return patch(path, userUuid, userId, null, body);
    }

    protected ResponseEntity<Object> patch(String path, UUID userUuid, Long userId, @Nullable Map<String, Object> parameters) {
        return patch(path, userUuid, userId, parameters, null);
    }

    protected <T> ResponseEntity<Object> patch(String path, UUID userUuid, Long userId, @Nullable Map<String, Object> parameters, T body) {
        return executeRestCall(HttpMethod.PATCH, path, userUuid, userId, parameters, body);
    }

    // delete
    protected ResponseEntity<Object> delete(String path) {
        return delete(path, null);
    }

    protected ResponseEntity<Object> delete(String path, UUID userUuid) {
        return delete(path, userUuid, null);
    }

    protected ResponseEntity<Object> delete(String path, UUID userUuid, Long userId) {
        return delete(path, userUuid, userId, null);
    }

    protected ResponseEntity<Object> delete(String path, UUID userUuid, Long userId, @Nullable Map<String, Object> parameters) {
        return executeRestCall(HttpMethod.DELETE, path, userUuid, userId, parameters, null);
    }

    private <T> ResponseEntity<Object> executeRestCall(HttpMethod method,
                                                       String path,
                                                       @Nullable UUID userUuid,
                                                       @Nullable Long userId,
                                                       @Nullable Map<String, Object> parameters,
                                                       @Nullable T body) {
        HttpEntity<T> requestEntity = new HttpEntity<>(body, defaultHeaders(userUuid, userId));
        ResponseEntity<Object> serverResponse;

        try {
            if (parameters != null) {
                serverResponse = rest.exchange(path, method, requestEntity, Object.class, parameters);
            } else {
                serverResponse = rest.exchange(path, method, requestEntity, Object.class);
            }
        } catch (HttpStatusCodeException e) {
            log.error("HTTP request failed with status code: {}", e.getStatusCode(), e);
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }

        return prepareResponse(serverResponse);
    }


    private HttpHeaders defaultHeaders(UUID userUuid, Long userId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        if (userUuid != null) {
            headers.set(X_USER_UUID, userUuid.toString());
        }
        if (userId != null) {
            headers.set(X_USER_ID, String.valueOf(userId));
        }

        return headers;
    }

    private static ResponseEntity<Object> prepareResponse(ResponseEntity<Object> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            return response;
        }

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());

        if (response.hasBody()) {
            return responseBuilder.body(response.getBody());
        }

        return responseBuilder.build();
    }
}
