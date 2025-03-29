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
import java.util.function.Function;
import java.util.function.Supplier;

@Slf4j
@Component
@RequiredArgsConstructor
public class ResponseHandler {
    private final ObjectMapper objectMapper;

    public <T> ApiResponse<T> processResponse(ResponseEntity<Object> response, Class<T> successType) {
        return processSingleType(response, successType, (resp) -> processSuccessResponse(resp, successType));
    }

    public <T> ApiResponse<List<T>> processResponseList(ResponseEntity<Object> response, Class<T> elementType) {
        return processListType(response, elementType, (resp) -> processSuccessResponseList(resp, elementType));
    }

    private <T> ApiResponse<T> processSingleType(ResponseEntity<Object> response,
                                                 Class<T> type,
                                                 Function<ResponseEntity<Object>, ApiResponse<T>> successHandler) {
        log.debug("Target type to convert response body: {}", type.getSimpleName());
        return processCommonLogic(response, () -> successHandler.apply(response));

    }

    private <T> ApiResponse<List<T>> processListType(ResponseEntity<Object> response,
                                                     Class<T> elementType,
                                                     Function<ResponseEntity<Object>, ApiResponse<List<T>>> successHandler) {
        log.debug("Target type to convert response body: List<{}>", elementType.getSimpleName());
        return processCommonLogic(response, () -> successHandler.apply(response));
    }

    private <R> R processCommonLogic(ResponseEntity<Object> response, Supplier<R> processor) {
        try {
            log.debug("Attempting to consider response as successful");
            return processor.get();
        } catch (TargetServiceHandledErrorException e) {
            log.error("Target service reported a handled error via class '{}', raising exception '{}': {}",
                    e.getError().getClass().getSimpleName(), e.getClass().getSimpleName(), e.getMessage());
            throw e;
        }
    }

    private <T> ApiResponse<T> processSuccessResponse(ResponseEntity<Object> response, Class<T> type) {
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

    private <T> ApiResponse<List<T>> processSuccessResponseList(ResponseEntity<Object> response, Class<T> elementType) {
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
