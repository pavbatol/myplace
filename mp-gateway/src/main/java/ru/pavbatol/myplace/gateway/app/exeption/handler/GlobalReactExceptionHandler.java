package ru.pavbatol.myplace.gateway.app.exeption.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.lang.NonNull;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import ru.pavbatol.myplace.gateway.app.api.ApiResponse;
import ru.pavbatol.myplace.gateway.app.exeption.ApiResponseException;
import ru.pavbatol.myplace.gateway.app.exeption.MissingHeaderException;
import ru.pavbatol.myplace.shared.dto.api.ApiError;
import ru.pavbatol.myplace.shared.exception.TargetServiceErrorException;
import ru.pavbatol.myplace.shared.exception.TargetServiceHandledErrorException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * Reactive exception handler that transforms exceptions into standardized API error responses.
 *
 * <p>Transition note: Currently coexists with {@link GlobalExceptionHandler} during migration
 * to reactive stack, with this implementation handling reactive endpoints specifically.
 *
 * <p>Produces error responses in format:
 * <pre>{@code
 * {
 *   "status": "HTTP_STATUS_CODE",
 *   "data": null,
 *   "error": {
 *     "timestamp": "2025-03-20T12:34:56Z",
 *     "mapping": "/api/users",
 *     "status": "HTTP_STATUS_CODE",
 *     "reason": "Error reason",
 *     "message": "Detailed message",
 *     "trace": ["..."]  // Only in non-production
 *   }
 * }}</pre>
 *
 * <p>Key differences from Servlet implementation:
 * <ul>
 *   <li>Handles reactive pipeline exceptions (WebFlux)</li>
 *   <li>Processes error signals in reactive streams</li>
 *   <li>Maintains same response format for consistency</li>
 * </ul>
 *
 * @see GlobalExceptionHandler The Servlet-based counterpart
 */
@Slf4j
@RestControllerAdvice
public class GlobalReactExceptionHandler implements ErrorWebExceptionHandler {
    private final boolean traceEnabled;
    private final ObjectMapper objectMapper;

    public GlobalReactExceptionHandler(Environment environment, ObjectMapper objectMapper) {
        this.traceEnabled = !environment.matchesProfiles("production");
        this.objectMapper = objectMapper;
    }

    @ExceptionHandler(ApiResponseException.class)
    public Mono<ResponseEntity<ApiResponse<Void>>> handleApiResponseException(ApiResponseException ex, ServerWebExchange webExchange) {
        HttpStatus httpStatus = ex.getStatus();
        ApiError apiError = createApiError(ex, webExchange, httpStatus);

        return Mono.just(buildErrorResponse(apiError, httpStatus));
    }

    @ExceptionHandler({TargetServiceHandledErrorException.class, TargetServiceErrorException.class})
    public Mono<ResponseEntity<ApiResponse<Void>>> handleApiResponseException(TargetServiceErrorException ex) {
        HttpStatus httpStatus = ex.getStatus();
        ApiError apiError = ex.getError();

        return Mono.just(buildErrorResponse(apiError, httpStatus));
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public Mono<ResponseEntity<ApiResponse<Void>>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, ServerWebExchange webExchange) {
        HttpStatus httpStatus = determineStatus(ex);
        ApiError apiError = createApiError(ex, webExchange, httpStatus);

        return Mono.just(buildErrorResponse(apiError, httpStatus));
    }

    @ExceptionHandler({MissingHeaderException.class})
    public Mono<ResponseEntity<ApiResponse<Void>>> handleMethodArgumentNotValidException(MissingHeaderException ex, ServerWebExchange webExchange) {
        HttpStatus httpStatus = BAD_REQUEST;
        ApiError apiError = createApiError(ex, webExchange, httpStatus);

        return Mono.just(buildErrorResponse(apiError, httpStatus));
    }

    @ExceptionHandler(Throwable.class)
    public Mono<ResponseEntity<ApiResponse<Void>>> handleUncaughtException(Throwable ex, ServerWebExchange webExchange) {
        log.debug("Uncaught exception handler triggered");
        HttpStatus httpStatus = determineStatus(ex);

        ApiError apiError = createApiError(ex, webExchange, httpStatus);

        return Mono.just(buildErrorResponse(apiError, httpStatus));
    }

    @NonNull
    public Mono<Void> handle(@NonNull ServerWebExchange exchange, @NonNull Throwable ex) {
        log.debug("WebFlux exception handler triggered");
        return handleUncaughtException(ex, exchange)
                .flatMap(responseEntity -> {
                    exchange.getResponse().setStatusCode(responseEntity.getStatusCode());
                    exchange.getResponse().getHeaders().addAll(responseEntity.getHeaders());

                    if (responseEntity.getBody() != null) {
                        byte[] bytes = serializeToJson(responseEntity.getBody());
                        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
                        return exchange.getResponse().writeWith(Mono.just(buffer));
                    } else {
                        return exchange.getResponse().setComplete();
                    }
                });
    }

    private byte[] serializeToJson(Object body) {
        try {
            return objectMapper.writeValueAsBytes(body);
        } catch (JsonProcessingException e) {
            return "{\"error\": \"Failed to serialize response\"}".getBytes();
        }
    }

    private ApiError createApiError(Throwable ex, ServerWebExchange webExchange, HttpStatus httpStatus) {
        return createApiError(null, ex, webExchange, httpStatus);
    }

    private ApiError createApiError(String details, Throwable ex, ServerWebExchange webExchange, HttpStatus httpStatus) {
        String reason = ex.getCause() != null
                ? String.format("%s: %s", ex.getCause().getClass().getSimpleName(), ex.getCause().getMessage())
                : null;

        String message = String.format("%s: %s", ex.getClass().getSimpleName(), ex.getMessage());

        List<String> errors = (ex instanceof BindException) ? ((BindException) ex).getAllErrors().stream()
                .map(this::getErrorString)
                .collect(Collectors.toList())
                : null;

        List<String> trace = traceEnabled ? Arrays.stream(ex.getStackTrace())
                .map(StackTraceElement::toString).collect(Collectors.toList())
                : null;

        return new ApiError(
                getFullRequestURI(webExchange),
                httpStatus.toString(),
                reason,
                message,
                details,
                errors,
                trace
        );
    }

    private HttpStatus determineStatus(Throwable ex) {
        if (ex instanceof HttpStatusCodeException) {
            return ((HttpStatusCodeException) ex).getStatusCode();
        } else if (ex instanceof ResponseStatusException) {
            return ((ResponseStatusException) ex).getStatus();
        } else {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

    private String getErrorString(ObjectError error) {
        if (error instanceof FieldError) {
            FieldError fieldError = (FieldError) error;

            String rejectedValue = String.valueOf(fieldError.getRejectedValue());
            return String.format("Field: %s. Error: %s. Value: %s",
                    fieldError.getField(),
                    fieldError.getDefaultMessage(),
                    rejectedValue);
        }
        return error.getDefaultMessage();
    }

    private String getFullRequestURI(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        return String.format("%s %s?%s",
                request.getMethod(),
                request.getPath(),
                request.getQueryParams());
    }

    private <T> ResponseEntity<ApiResponse<T>> buildErrorResponse(ApiError apiError, HttpStatus httpStatus) {
        ApiResponse<T> apiResponse = ApiResponse.error(apiError, httpStatus);
        log.error("\u001B[31mError occurred: {}\u001B[0m", apiError.getMessage());

        return ResponseEntity.status(httpStatus).body(apiResponse);
    }
}
