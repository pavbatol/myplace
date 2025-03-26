package ru.pavbatol.myplace.shared.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.util.MimeType;
import ru.pavbatol.myplace.shared.dto.api.ErrorResponse;
import ru.pavbatol.myplace.shared.exception.ApiErrorException;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class BaseRestClientBodyParser {
    private final ObjectMapper objectMapper;

    @Nullable
    public <T> T parse(ResponseEntity<Object> response, Class<T> type) throws IOException {
        if (!response.getStatusCode().is2xxSuccessful()) {
            ErrorResponse error = parseError(response);
            throw new ApiErrorException(error, response.getStatusCode());
        }

        return parseSuccessBody(response, type);
    }

    private <T> T parseSuccessBody(ResponseEntity<Object> response, Class<T> type) throws IOException {
        Object body = response.getBody();
        if (body == null) {
            return null;
        }

        try {
            if (body instanceof byte[]) {
                if (byte[].class.equals(type)) {
                    return (T) body;
                }

                if (String.class.equals(type)) {
                    Charset charset = Optional.ofNullable(response.getHeaders().getContentType())
                            .map(MimeType::getCharset)
                            .orElse(StandardCharsets.UTF_8);
                    return (T) new String((byte[]) body, charset);
                }

                return objectMapper.readValue((byte[]) body, type);
            } else if (body instanceof String) {
                if (String.class.equals(type)) {
                    return (T) body;
                }

                return objectMapper.readValue((String) body, type);
            } else {
                return objectMapper.convertValue(body, type);
            }
        } catch (IOException e) {
            log.error("Failed to parse response to type {}. Body type: {}", type.getSimpleName(), body.getClass().getSimpleName());
            throw e;
        }
    }

    private ErrorResponse parseError(ResponseEntity<Object> response) throws IOException {
        try {
            return objectMapper.readValue((byte[]) response.getBody(), ErrorResponse.class);
        } catch (Exception e) {
            log.error("Failed to parse error response", e);
            return new ErrorResponse(
                    null,
                    null,
                    null,
                    "Failed to parse error body: " + e.getMessage(),
                    null,
                    null,
                    null
            );
        }
    }

    public <T> List<T> parseList(ResponseEntity<Object> response, Class<T> elementType) throws JsonProcessingException {

        // TODO: Implementation in progress (currently stubbed)

//        return objectMapper.readValue(serialized, objectMapper.getTypeFactory().constructCollectionType(List.class, elementType));
        return null;
    }
}
