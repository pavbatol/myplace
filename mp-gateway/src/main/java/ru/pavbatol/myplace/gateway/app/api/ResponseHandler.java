package ru.pavbatol.myplace.gateway.app.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ResponseHandler {
    private final ObjectMapper objectMapper;

    public <T> ApiResponse<T> processResponse(ResponseEntity<Object> response, Class<T> successType) {
        if (response.getStatusCode().is2xxSuccessful()) {
            return Optional.ofNullable(response.getBody())
                    .map(body -> {
                        String jsonBody = serializeBody(body);
                        try {
                            T successResponse = objectMapper.readValue(jsonBody, successType);
                            return ApiResponse.success(successResponse, response.getStatusCode());
                        } catch (JsonProcessingException e) {
                            log.error("Failed to parse error response body: {} into {}", body.toString(), successType.getSimpleName(), e);
                            throw new RuntimeException("Failed to parse response body", e);
                        }
                    })
                    .orElseGet(() -> ApiResponse.status(response.getStatusCode()));
        } else {
            return Optional.ofNullable(response.getBody())
                    .map(body -> {
                        String jsonBody = serializeBody(body);
                        try {
                            ApiError apiError = objectMapper.readValue(jsonBody, ApiError.class);
                            return ApiResponse.<T>error(apiError, response.getStatusCode());
                        } catch (JsonProcessingException e) {
                            log.error("Failed to parse error response body: {} into {}", jsonBody, ApiError.class.getSimpleName(), e);
                            return (jsonBody.equals("\"\""))
                                    ? ApiResponse.<T>status(response.getStatusCode())
                                    : ApiResponse.<T>error(
                                    new ApiError(null, null, null, jsonBody, null, null, null),
                                    response.getStatusCode());
                        }
                    })
                    .orElseGet(() -> {
                        log.warn("No response body for error status: {}", response.getStatusCode());
                        return ApiResponse.status(response.getStatusCode());
                    });
        }
    }

    private String serializeBody(Object body) {
        try {
            return (body instanceof String) ? (String) body : objectMapper.writeValueAsString(body);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize body: " + body, e);
        }
    }
}
