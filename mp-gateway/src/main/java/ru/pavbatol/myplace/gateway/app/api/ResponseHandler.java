package ru.pavbatol.myplace.gateway.app.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ResponseHandler {
    private final ObjectMapper objectMapper;

    public <T> ApiResponse<T> processResponse(ResponseEntity<Object> response, Class<T> successType) {
        log.debug("Target type to convert response body: {}", successType);
        if (response.getStatusCode().is2xxSuccessful()) {
            return Optional.ofNullable(response.getBody())
                    .map(body -> {
                        String preparedBody = convertBodyToString(body);
                        logBodyInfo(successType, body, preparedBody);

                        if (successType.equals(String.class)) {
                            return ApiResponse.success((T) preparedBody, response.getStatusCode());
                        }

                        try {
                            T successResponse = objectMapper.readValue(preparedBody, successType);
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
                        String preparedBody = convertBodyToString(body);
                        logBodyInfo(successType, body, preparedBody);
                        try {
                            ApiError apiError = objectMapper.readValue(preparedBody, ApiError.class);
                            return ApiResponse.<T>error(apiError, response.getStatusCode());
                        } catch (JsonProcessingException e) {
                            log.error("Failed to parse error response body: {} into {}", preparedBody, ApiError.class.getSimpleName(), e);
                            return (preparedBody.equals("\"\""))
                                    ? ApiResponse.<T>status(response.getStatusCode())
                                    : ApiResponse.<T>error(
                                    new ApiError(null, null, null, preparedBody, null, null, null),
                                    response.getStatusCode());
                        }
                    })
                    .orElseGet(() -> {
                        log.warn("No response body for error status: {}", response.getStatusCode());
                        return ApiResponse.status(response.getStatusCode());
                    });
        }
    }

    private <T> void logBodyInfo(Class<T> successType, Object body, String preparedBody) {
        log.debug("Raw body}: {}: ", body);
        log.debug("Prepared body to convert into {}: {}: ", successType, preparedBody);
    }

    private String convertBodyToString(Object body) {
        if (body instanceof byte[]) {
            return new String((byte[]) body, StandardCharsets.UTF_8);
        } else if (body instanceof String) {
            return (String) body;
        } else {
            try {
                return objectMapper.writeValueAsString(body);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Failed to serialize body: " + body, e);
            }
        }
    }
}
