package ru.pavbatol.myplace.shared.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.MimeType;
import ru.pavbatol.myplace.shared.dto.api.ApiError;
import ru.pavbatol.myplace.shared.exception.TargetServiceErrorException;
import ru.pavbatol.myplace.shared.exception.TargetServiceHandledErrorException;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

/**
 * Parses HTTP response bodies into Java objects using Jackson ObjectMapper.
 * Supports:
 * <ul>
 *   <li>JSON and text responses (String/byte[])</li>
 *   <li>Direct object conversion</li>
 *   <li>Error response handling</li>
 *   <li>Collection parsing</li>
 * </ul>
 *
 * <p>Automatically handles:
 * <ul>
 *   <li>Content-Type charset detection</li>
 *   <li>Error response parsing</li>
 *   <li>Null response bodies</li>
 * </ul>
 *
 * @see ObjectMapper
 * @see ResponseEntity
 */
@Slf4j
@RequiredArgsConstructor
public class ResponseBodyParser {
    private final ObjectMapper objectMapper;

    /**
     * Parses response body into specified type
     *
     * @param <T>      Target type
     * @param response HTTP response entity
     * @param type     Target class to parse into
     * @return Parsed object or null if response body is empty
     * @throws TargetServiceHandledErrorException if response status is not 2xx
     * @throws IOException                        if parsing fails or response contains invalid content
     * @throws IllegalArgumentException           if conversion is not possible
     */
    @Nullable
    public <T> T parse(ResponseEntity<Object> response, Class<T> type) throws IOException {
        if (!response.getStatusCode().is2xxSuccessful()) {
            handleErrorResponse(response);
        }

        return parseSuccessBody(response, type);
    }

    /**
     * Parses response body into List of specified element type
     *
     * @param <T>         List element type
     * @param response    HTTP response entity
     * @param elementType List element class
     * @return List of parsed elements (empty list if response body is null)
     * @throws TargetServiceHandledErrorException if response status is not 2xx
     * @throws IOException                        if parsing fails or response contains invalid content
     * @throws IllegalArgumentException           if conversion is not possible
     */
    @NonNull
    public <T> List<T> parseList(ResponseEntity<Object> response, Class<T> elementType) throws IOException {
        if (!response.getStatusCode().is2xxSuccessful()) {
            handleErrorResponse(response);
        }

        Object body = response.getBody();
        if (body == null) {
            return List.of();
        }

        CollectionType collectionType = objectMapper.getTypeFactory().constructCollectionType(List.class, elementType);

        try {
            if (body instanceof byte[]) {
                return objectMapper.readValue((byte[]) body, collectionType);
            } else if (body instanceof String) {
                return objectMapper.readValue((String) body, collectionType);
            } else {
                return objectMapper.convertValue(body, collectionType);
            }
        } catch (JsonProcessingException e) {
            throw new IOException("Failed to parse list of " + elementType.getSimpleName(), e);
        } catch (IllegalArgumentException e) {
            throw new IOException("Cannot convert body to List<" + elementType.getSimpleName() + ">", e);
        }
    }

    /**
     * Parses successful (2xx) response body
     *
     * @param <T>      Target type
     * @param response HTTP response entity
     * @param type     Target class to parse into
     * @return Parsed object or null if response body is empty
     * @throws IOException if parsing fails
     */
    private <T> T parseSuccessBody(ResponseEntity<Object> response, Class<T> type) throws IOException {
        Object body = response.getBody();
        if (body == null) {
            return null;
        }

        if (Void.class.equals(type)) {
            log.warn("Void response with non-null body: {}", body);
            throw new IllegalArgumentException("Cannot convert non-null body to Void");
        }

        try {
            if (body instanceof byte[]) {
                return parseByteArrayBody((byte[]) body, response.getHeaders().getContentType(), type);
            } else if (body instanceof String) {
                return parseStringBody((String) body, type);
            } else {
                return parseObjectBody(body, type);
            }
        } catch (JsonProcessingException e) {
            log.error("Failed to parse response body into type '{}': {}", type.getSimpleName(), e.getMessage());
            throw e;
        } catch (IOException e) {
            log.error("Failed to parse response to type '{}'. Body type: '{}'", type.getSimpleName(), body.getClass().getSimpleName());
            throw e;
        }
    }

    /**
     * Parses a byte array response body into the specified target type.
     *
     * <p>Handles three cases:
     * <ul>
     *   <li>If the target type is {@code byte[]}, returns the raw bytes.</li>
     *   <li>If the target type is {@code String}, converts bytes to a string using the charset from {@code contentType} (default: UTF-8).</li>
     *   <li>Otherwise, attempts JSON deserialization via {@link ObjectMapper}.</li>
     * </ul>
     *
     * @param body        the raw response body as a byte array (must not be {@code null})
     * @param contentType the {@link MediaType} of the response (may be {@code null} for default charset)
     * @param type        the target class to parse into (must not be {@code null})
     * @param <T>         the target type for deserialization
     * @return the parsed object, or raw bytes if {@code type == byte[].class}
     * @throws IOException              if parsing fails (e.g., invalid JSON or charset conversion error)
     * @throws IllegalArgumentException if {@code type} is {@code byte[]} but {@code body} is malformed
     */
    private <T> T parseByteArrayBody(byte[] body, @Nullable MediaType contentType, Class<T> type) throws IOException {
        if (byte[].class.equals(type)) {
            log.debug("Parsed body will be returned as raw bytes");
            return (T) body;
        }

        if (String.class.equals(type)) {
            Charset charset = Optional.ofNullable(contentType)
                    .map(MimeType::getCharset)
                    .orElse(StandardCharsets.UTF_8);
            log.debug("Parsed body will be returned as converted bytes to raw String");

            @SuppressWarnings("unchecked")
            T result = (T) new String(body, charset);

            return result;
        }

        log.debug("Attempting to read body from '{}' to '{}'", byte[].class.getSimpleName(), type.getSimpleName());
        return objectMapper.readValue(body, type);
    }

    /**
     * Parses a String response body into the specified target type.
     *
     * <p>Two cases handled:
     * <ul>
     *     <li>If the target type is {@code String}, returns the raw String</li>
     *     <li>Attempts JSON deserialization via ObjectMapper.</li>
     * </ul>
     * </p>
     *
     * @param body the raw response body as a String (must not be {@code null})
     * @param type the target class to parse into (must not be {@code null})
     * @param <T>  the target type for deserialization
     * @return the parsed object, or raw String if {@code type == String.class}
     * @throws JsonProcessingException if parsing fails (invalid JSON)
     */

    /**
     * Parses a String response body into the specified target type.
     *
     * <p>Handles two cases:
     * <ul>
     *     <li>If the target type is {@code String}, returns the raw String unchanged</li>
     *     <li>Otherwise, attempts JSON deserialization using {@link ObjectMapper}</li>
     * </ul>
     *
     * @param body the raw response body as a String (must not be {@code null})
     * @param type the target class to parse into (must not be {@code null})
     * @param <T>  the target type for deserialization
     * @return the parsed object of type {@code T}, or the original String if {@code type == String.class}
     * @throws JsonProcessingException  if JSON parsing fails (malformed content)
     * @throws IllegalArgumentException if {@code type} is incompatible with the input
     */
    private <T> T parseStringBody(String body, Class<T> type) throws JsonProcessingException {
        if (String.class.equals(type)) {
            log.debug("Parsed body will be returned as raw String");

            @SuppressWarnings("unchecked")
            T result = (T) body;

            return result;
        }

        log.debug("Attempting to read body from 'String' to '{}'", type.getSimpleName());
        return objectMapper.readValue(body, type);
    }

    /**
     * Converts a structured response body (Map, List or other Object) into the specified target type.
     *
     * <p>Uses Jackson's {@link ObjectMapper#convertValue} to perform type conversion.
     * Typically handles cases where the body is already parsed as:
     * <ul>
     *     <li>{@link java.util.Map} (for JSON objects)</li>
     *     <li>{@link java.util.List} (for JSON arrays)</li>
     *     <li>Other pre-deserialized POJOs</li>
     * </ul>
     *
     * @param body the structured response body (Map, List or other Object) (must not be {@code null})
     * @param type the target class to convert into (must not be {@code null})
     * @param <T>  the target type for conversion
     * @return the converted object of type {@code T}
     * @throws IllegalArgumentException if conversion fails (incompatible types, etc.)
     * @throws IllegalStateException    if ObjectMapper configuration prevents conversion
     * @see ObjectMapper#convertValue(Object, Class)
     */
    private <T> T parseObjectBody(Object body, Class<T> type) {
        log.debug("Attempting to convert body from 'Object' (e.g.: Map, List) to '{}'", type.getSimpleName());
        return objectMapper.convertValue(body, type);
    }

    /**
     * Parses error response body into {@link ApiError} structure.
     * Returns default error response if not valid body or parsing fails.
     * <p>
     * Note: All error responses from the client are received as byte arrays
     * ({@code byte[]}) before parsing. This is handled by the {@link BaseRestClient}.
     * </p>
     *
     * @param response HTTP error response
     * @return {@link  ErrorParseResult} with parsed received {@link ApiError} or created default that if body is invalid
     */
    private ErrorParseResult parseError(ResponseEntity<Object> response) {
        final Object body = response.getBody();
        final HttpStatus status = response.getStatusCode();

        if (body == null) {
            return invalidError("Null error response body", status);
        }

        if (!(body instanceof byte[])) {
            return invalidError("Unexpected error body type: " + body.getClass().getSimpleName(), status);
        }

        byte[] bodyBytes = (byte[]) body;
        if (bodyBytes.length == 0) {
            return invalidError("Empty error response body", status);
        }

        try {
            ErrorParseResult result = new ErrorParseResult(objectMapper.readValue(bodyBytes, ApiError.class), false);
            log.debug("Successfully parsed body to {}", ApiError.class.getSimpleName());
            return result;
        } catch (IOException e) {
            log.error("Failed to parse error response", e);
            return new ErrorParseResult(
                    createDefaultError("Failed to parse error: " + e.getMessage(), status),
                    true);
        }
    }

    /**
     * Helper for {@link #parseError} to handle invalid response cases.
     * Logs the issue and returns a default error representation.
     * <p>
     * Guarantees:
     *   <ul>
     *     <li>Will always log the failure at WARN level</li>
     *     <li>Will always return a non-null result with errorLocalCreated=true</li>
     *   </ul>
     * </p>
     */
    private ErrorParseResult invalidError(String reason, HttpStatus status) {
        log.warn("Invalid error response ({}), status {}", reason, status);
        return new ErrorParseResult(createDefaultError(reason, status), true);
    }

    /**
     * Creates default ErrorResponse with basic information
     */
    private ApiError createDefaultError(String message, HttpStatus status) {
        return new ApiError(
                null,
                status.toString(),
                null,
                message,
                null,
                null,
                null
        );
    }

    /**
     * Transforms target service error responses into exceptions.
     * <p>
     * This method always throws an exception when processing error responses:
     * <ul>
     *   <li>For well-formed errors - throws {@link TargetServiceHandledErrorException}</li>
     *   <li>For malformed responses - throws {@link TargetServiceErrorException}</li>
     * </ul>
     * <p>
     *  @throws TargetServiceHandledErrorException when service returned a properly formatted error via {@link ApiError}
     *  @throws TargetServiceErrorException when service returned a raw (unhandled) error or unprocessable
     */
    private void handleErrorResponse(ResponseEntity<Object> response) {
        ErrorParseResult result = parseError(response);
        ApiError error = result.getApiError();

        if (result.isErrorLocalCreated()) {
            throw new TargetServiceErrorException(error, response.getStatusCode()
            );
        }

        throw new TargetServiceHandledErrorException(error, response.getStatusCode());
    }

    @Value
    private static class ErrorParseResult {
        ApiError apiError;
        boolean errorLocalCreated;
    }
}

