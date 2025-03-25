package ru.pavbatol.myplace.gateway.app.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.pavbatol.myplace.gateway.app.exeption.ApiResponseException;

import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RequiredArgsConstructor
public class ResponseHandler {
    private final ObjectMapper objectMapper;

    public <T> ApiResponse<T> processResponse(ResponseEntity<Object> response, Class<T> successType) {
        log.debug("Target type to convert response body: {}", successType);

        if (response.getStatusCode().is2xxSuccessful()) {
            return processSuccessResponse(response, successType);
        } else {
            return processErrorResponse(response);
        }
    }

    private <T> ApiResponse<T> processSuccessResponse(ResponseEntity<Object> response, Class<T> successType) {
        Object body = response.getBody();
        if (body == null) {
            return ApiResponse.status(response.getStatusCode());
        }

        String preparedBody = serializeBodyToString(body);

        if (String.class.equals(successType)) {
            log.debug("Response will be returned as raw String");
            return ApiResponse.success((T) preparedBody, response.getStatusCode());
        }

        try {
            log.debug("Attempting to deserialize response into type: {}", successType.getSimpleName());
            T successResponse = objectMapper.readValue(preparedBody, successType);
            return ApiResponse.success(successResponse, response.getStatusCode());
        } catch (JsonProcessingException e) {
            log.error("Failed to deserialize response body into type {}: {}", successType.getSimpleName(), e.getMessage());
            throw new ApiResponseException("Failed to parse API response", HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }

    private <T> ApiResponse<T> processErrorResponse(ResponseEntity<Object> response) {
        Object body = response.getBody();
        if (body == null) {
            log.warn("No response body for error status: {}", response.getStatusCode());
            return ApiResponse.status(response.getStatusCode());
        }

        String preparedBody = serializeBodyToString(body);

        try {
            ApiError apiError = objectMapper.readValue(preparedBody, ApiError.class);
            return ApiResponse.error(apiError, response.getStatusCode());
        } catch (JsonProcessingException e) {
            log.error("Failed to parse error response body: {} into {}", preparedBody, ApiError.class.getSimpleName(), e);
            return ApiResponse.error(
                    new ApiError(
                            null,
                            null,
                            null,
                            preparedBody,
                            "Failed to parse error response body — showing raw response instead (see 'message' field). " +
                                    "Original status code: " + response.getStatusCode(),
                            null,
                            null
                    ),
                    response.getStatusCode()
            );
        }
    }

    private String serializeBodyToString(Object body) {
        if (body == null) {
            return "";
        }

        if (body instanceof String) {
            return (String) body;
        } else if (body instanceof byte[]) {
            return new String((byte[]) body, StandardCharsets.UTF_8);
        } else {
            try {
                return objectMapper.writeValueAsString(body);
            } catch (JsonProcessingException e) {
                throw new ApiResponseException("Failed to serialize body", HttpStatus.INTERNAL_SERVER_ERROR, e);
            }
        }
    }
}
