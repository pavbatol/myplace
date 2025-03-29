package ru.pavbatol.myplace.gateway.app.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.pavbatol.myplace.gateway.app.exeption.ApiResponseException;
import ru.pavbatol.myplace.shared.client.ResponseBodyParser;
import ru.pavbatol.myplace.shared.exception.TargetServiceHandledErrorException;

import java.io.IOException;
import java.util.List;
import java.util.function.Supplier;

@Slf4j
@Component
@RequiredArgsConstructor
public class ResponseHandler {
    private final ObjectMapper objectMapper;

    public <T> ApiResponse<T> processResponse(ResponseEntity<Object> response, Class<T> type) {
        log.debug("Target type to convert response body: {}", type.getSimpleName());
        return processCommonLogic(response, () -> processSingleType(response, type));
    }

    public <T> ApiResponse<List<T>> processResponseList(ResponseEntity<Object> response, Class<T> elementType) {
        log.debug("Target type to convert response body: List<{}>", elementType.getSimpleName());
        return processCommonLogic(response, () -> processListType(response, elementType));
    }

    private <R> R processCommonLogic(ResponseEntity<Object> response, Supplier<R> processor) {
        try {
            log.debug("Attempting to consider response as successful");
            R result = processor.get();
            log.debug("Successfully processed response");
            return result;
        } catch (TargetServiceHandledErrorException e) {
            log.error("Target service reported a handled error via class '{}', raising exception '{}': {}",
                    e.getError().getClass().getSimpleName(), e.getClass().getSimpleName(), e.getMessage());
            throw e;
        }
    }

    private <T> ApiResponse<T> processSingleType(ResponseEntity<Object> response, Class<T> type) {
        ResponseBodyParser bodyParser = new ResponseBodyParser(objectMapper);

        try {
            T parsedBody = bodyParser.parse(response, type);
            if (parsedBody == null) {
                return ApiResponse.status(response.getStatusCode());
            }

            return ApiResponse.success(parsedBody, response.getStatusCode());
        } catch (IOException e) {
            log.error("Failed to deserialize response body into type {}: {}", type.getSimpleName(), e.getMessage());
            throw new ApiResponseException("Failed to parse API response", HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }

    private <T> ApiResponse<List<T>> processListType(ResponseEntity<Object> response, Class<T> elementType) {
        ResponseBodyParser bodyParser = new ResponseBodyParser(objectMapper);

        try {
            List<T> parsedBody = bodyParser.parseList(response, elementType);
            return ApiResponse.success(parsedBody, response.getStatusCode());
        } catch (IOException e) {
            log.error("Failed to deserialize response body into type 'List' with elements type of '{}': {}",
                    elementType.getSimpleName(), e.getMessage());
            throw new ApiResponseException("Failed to parse API response", HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }
}
