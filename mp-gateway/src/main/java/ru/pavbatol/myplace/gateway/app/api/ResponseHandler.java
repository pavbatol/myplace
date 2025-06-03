package ru.pavbatol.myplace.gateway.app.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.pavbatol.myplace.gateway.app.exeption.ApiResponseException;
import ru.pavbatol.myplace.shared.client.ResponseBodyParser;
import ru.pavbatol.myplace.shared.dto.pagination.SimpleSlice;
import ru.pavbatol.myplace.shared.exception.TargetServiceErrorException;
import ru.pavbatol.myplace.shared.exception.TargetServiceHandledErrorException;

import java.io.IOException;
import java.util.List;
import java.util.function.Supplier;

/**
 * Handles HTTP responses by converting ResponseEntity objects into typed ApiResponse objects.
 * Supports processing of both single objects and collections.
 *
 * <p>Key features:</p>
 * <ul>
 *   <li>Parses response body into specified type</li>
 *   <li>Handles server errors and malformed responses</li>
 *   <li>Provides detailed logging of processing stages</li>
 *   <li>Maintains type safety through generics</li>
 * </ul>
 *
 * @see ApiResponse
 * @see ResponseBodyParser
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ResponseHandler {
    private final ObjectMapper objectMapper;
    private final ResponseBodyParser bodyParser;

    /**
     * Processes a response containing a single object.
     *
     * @param <T>      the type of the expected response object
     * @param response the HTTP response to process
     * @param type     the class object representing the expected response type
     * @return ApiResponse containing the parsed object or error details
     * @throws ApiResponseException               if response parsing fails
     * @throws TargetServiceHandledErrorException if the target service returns a handled error
     * @throws TargetServiceErrorException        if the target service returns a raw (unhandled) error
     *                                            or if parsing failed and the error was created locally with the raw response received
     */
    public <T> ApiResponse<T> processResponse(ResponseEntity<Object> response, Class<T> type) {
        log.debug("Target type to convert response body: {}", type.getSimpleName());
        return processCommonLogic(response, () -> processSingleType(response, type));
    }

    /**
     * Processes a response containing a collection of objects.
     *
     * @param <T>         the type of elements in the expected list
     * @param response    the HTTP response to process
     * @param elementType the class object representing the list element type
     * @return ApiResponse containing the parsed list or error details
     * @throws ApiResponseException               if response parsing fails
     * @throws TargetServiceHandledErrorException if the target service returns a handled error
     * @throws TargetServiceErrorException        if the target service returns a raw (unhandled) error
     *                                            or if parsing failed and the error was created locally with the raw response received
     */
    public <T> ApiResponse<List<T>> processResponseList(ResponseEntity<Object> response, Class<T> elementType) {
        log.debug("Target type to convert response body: List<{}>", elementType.getSimpleName());
        return processCommonLogic(response, () -> processListType(response, elementType));
    }

    /**
     * Processes a response containing a {@link SimpleSlice} .
     *
     * @param <T>         the type of elements in the expected SimpleSlice
     * @param response    the HTTP response to process
     * @param elementType the class object representing the SimpleSlice element type
     * @return ApiResponse containing the parsed SimpleSlice or error details
     * @throws ApiResponseException               if response parsing fails
     * @throws TargetServiceHandledErrorException if the target service returns a handled error
     * @throws TargetServiceErrorException        if the target service returns a raw (unhandled) error
     *                                            or if parsing failed and the error was created locally with the raw response received
     */
    public <T> ApiResponse<SimpleSlice<T>> processResponseSimpleSlice(ResponseEntity<Object> response, Class<T> elementType) {
        log.debug("Target type to convert response body: Slice<{}>", elementType.getSimpleName());
        return processCommonLogic(response, () -> processSimpleSliceType(response, elementType));
    }

    /**
     * Common processing logic for all response types.
     * Handles logging and exception processing for consistent behavior.
     *
     * @param <R>       the expected return type
     * @param response  the HTTP response to process
     * @param processor the function that performs type-specific processing
     * @return the processed result
     * @throws TargetServiceHandledErrorException if the target service returns a handled error
     * @throws TargetServiceErrorException        if the target service returns a raw (unhandled) error
     *                                            or if parsing failed and the error was created locally with the raw response received
     */
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
        } catch (TargetServiceErrorException e) {
            log.error("Target service reported a raw (unhandled) error, raising exception '{}': {}",
                    e.getClass().getSimpleName(), e.getMessage());
            throw e;
        }
    }

    /**
     * Processes a response expecting a single object of specified type.
     *
     * @param <T>      the expected response type
     * @param response the HTTP response to process
     * @param type     the class object representing the expected type
     * @return ApiResponse containing the parsed object
     * @throws ApiResponseException if JSON parsing fails
     */
    private <T> ApiResponse<T> processSingleType(ResponseEntity<Object> response, Class<T> type) {
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

    /**
     * Processes a response expecting a list of objects of specified type.
     *
     * @param <T>         the type of elements in the expected list
     * @param response    the HTTP response to process
     * @param elementType the class object representing the list element type
     * @return ApiResponse containing the parsed list
     * @throws ApiResponseException if JSON parsing fails
     */
    private <T> ApiResponse<List<T>> processListType(ResponseEntity<Object> response, Class<T> elementType) {
        try {
            List<T> parsedBody = bodyParser.parseList(response, elementType);
            return ApiResponse.success(parsedBody, response.getStatusCode());
        } catch (IOException e) {
            log.error("Failed to deserialize response body into type 'List' with elements type of '{}': {}",
                    elementType.getSimpleName(), e.getMessage());
            throw new ApiResponseException("Failed to parse API response", HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }

    private <T> ApiResponse<SimpleSlice<T>> processSimpleSliceType(ResponseEntity<Object> response, Class<T> elementType) {
        try {
            SimpleSlice<T> parsedBody = bodyParser.parseSimpleSlice(response, elementType);
            return ApiResponse.success(parsedBody, response.getStatusCode());
        } catch (IOException e) {
            log.error("Failed to deserialize response body into type 'Slice' with elements type of '{}': {}",
                    elementType.getSimpleName(), e.getMessage());
            throw new ApiResponseException("Failed to parse API response", HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }
}
