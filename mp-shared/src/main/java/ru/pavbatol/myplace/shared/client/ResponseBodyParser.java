package ru.pavbatol.myplace.shared.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.MimeType;
import ru.pavbatol.myplace.shared.dto.api.ErrorResponse;
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
            ErrorResponse error = parseError(response);
            throw new TargetServiceHandledErrorException(error, response.getStatusCode());
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
            ErrorResponse error = parseError(response);
            throw new TargetServiceHandledErrorException(error, response.getStatusCode());
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

        try {
            if (body instanceof byte[]) {
                if (byte[].class.equals(type)) {
                    log.debug("Parsed body will be returned as raw bytes");
                    return (T) body;
                }

                if (String.class.equals(type)) {
                    Charset charset = Optional.ofNullable(response.getHeaders().getContentType())
                            .map(MimeType::getCharset)
                            .orElse(StandardCharsets.UTF_8);
                    log.debug("Parsed body will be returned as converted bytes to raw String");
                    return (T) new String((byte[]) body, charset);
                }

                log.debug("Attempting to read body from '{}' to '{}'", byte[].class.getSimpleName(), type.getSimpleName());
                return objectMapper.readValue((byte[]) body, type);
            } else if (body instanceof String) {
                if (String.class.equals(type)) {
                    log.debug("Parsed body will be returned as raw String");
                    return (T) body;
                }

                log.debug("Attempting to read body from 'String' to '{}'", type.getSimpleName());
                return objectMapper.readValue((String) body, type);
            } else {
                log.debug("Attempting to convert body from 'Object' (e.g.: Map, List) to '{}'", type.getSimpleName());
                return objectMapper.convertValue(body, type);
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
     * Parses error response body into ErrorResponse structure.
     * Returns default error response if parsing fails.
     *
     * @param response HTTP error response
     * @return Parsed ErrorResponse or default error if body is null/malformed
     * @throws IOException only if critical parsing error occurs
     */
    private ErrorResponse parseError(ResponseEntity<Object> response) throws IOException {
        Object body = response.getBody();
        if (body == null) {
            log.warn("Empty error response body with status {}", response.getStatusCode());
            return createDefaultError("Empty error response body");
        }

        try {
            return objectMapper.readValue((byte[]) response.getBody(), ErrorResponse.class);
        } catch (Exception e) {
            log.error("Failed to parse error response", e);
            return createDefaultError("Failed to parse error: " + e.getMessage());
        }
    }

    /**
     * Creates default ErrorResponse with basic information
     */
    private ErrorResponse createDefaultError(String message) {
        return new ErrorResponse(
                null,
                null,
                null,
                message,
                null,
                null,
                null
        );
    }
}
