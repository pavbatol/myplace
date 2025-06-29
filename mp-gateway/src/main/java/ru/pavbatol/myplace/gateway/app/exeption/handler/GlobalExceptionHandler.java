package ru.pavbatol.myplace.gateway.app.exeption.handler;

import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import ru.pavbatol.myplace.gateway.app.api.ApiResponse;
import ru.pavbatol.myplace.gateway.app.exeption.ApiResponseException;
import ru.pavbatol.myplace.shared.dto.api.ApiError;
import ru.pavbatol.myplace.shared.exception.TargetServiceErrorException;
import ru.pavbatol.myplace.shared.exception.TargetServiceHandledErrorException;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * Global exception handler for REST controllers.
 * Transforms exceptions into standardized responses wrapped in
 * {@code ResponseEntity<ApiResponse<Void>>}.
 *
 * <p><strong>Typical error response format:</strong>
 * <pre>{@code
 * {
 *   "status": "HTTP_STATUS_CODE",
 *   "data": null,                  // Optional (rarely used for errors)
 *   "error": {
 *     "timestamp": "2025-03-20T12:34:56Z",
 *     "mapping": "/api/users",
 *     "status": "HTTP_STATUS_CODE",
 *     "reason": "Validation failed",
 *     "message": "Email must be valid",
 *     "trace": ["..."]             // Optional stack trace
 *   }
 * }
 * }</pre>
 *
 * <p><strong>Note:</strong>
 * <ul>
 *   <li>For errors, the {@code data} field is typically omitted or {@code null},
 *       but the structure allows flexibility if needed.</li>
 *   <li>{@code error.trace} is included only in non-production environments.</li>
 * </ul>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private final boolean traceEnabled;

    public GlobalExceptionHandler(Environment environment) {
        this.traceEnabled = !environment.matchesProfiles("production");
    }

    @ExceptionHandler(ApiResponseException.class)
    public ResponseEntity<ApiResponse<Void>> handleApiResponseException(ApiResponseException ex, WebRequest webRequest) {
        HttpStatus httpStatus = ex.getStatus();
        ApiError apiError = createApiError(ex, webRequest, httpStatus);

        ApiResponse<Void> apiResponse = ApiResponse.error(apiError, httpStatus);

        return ResponseEntity.status(httpStatus).body(apiResponse);
    }

    @ExceptionHandler({TargetServiceHandledErrorException.class, TargetServiceErrorException.class})
    public ResponseEntity<ApiResponse<Void>> handleApiResponseException(TargetServiceErrorException ex, WebRequest webRequest) {
        HttpStatus httpStatus = ex.getStatus();
        ApiError apiError = ex.getError();

        ApiResponse<Void> apiResponse = ApiResponse.error(apiError, httpStatus);

        return ResponseEntity.status(httpStatus).body(apiResponse);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    protected ResponseEntity<Object> handleMethodArgumentNotValidEx(MethodArgumentNotValidException ex, WebRequest webRequest) {
        HttpStatus httpStatus = determineStatus(ex);
        ApiError apiError = createApiError(ex, webRequest, httpStatus);

        ApiResponse<Void> apiResponse = ApiResponse.error(apiError, httpStatus);

        return ResponseEntity.status(BAD_REQUEST).body(apiResponse);
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ApiResponse<Void>> handleUncaughtException(Throwable ex, WebRequest webRequest) {
        HttpStatus httpStatus = determineStatus(ex);
        ApiError apiError = createApiError(ex, webRequest, httpStatus);

        ApiResponse<Void> apiResponse = ApiResponse.error(apiError, httpStatus);

        return ResponseEntity.status(httpStatus).body(apiResponse);
    }

    private ApiError createApiError(Throwable ex, WebRequest webRequest, HttpStatus httpStatus) {
        return createApiError(null, ex, webRequest, httpStatus);
    }

    private ApiError createApiError(String details, Throwable ex, WebRequest webRequest, HttpStatus httpStatus) {
        String reason = ex.getCause() != null ?
                String.format("%s: %s", ex.getCause().getClass().getSimpleName(), ex.getCause().getMessage())
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
                getRequestURI(webRequest),
                httpStatus.toString(),
                reason,
                message,
                details,
                errors,
                trace
        );
    }

    private HttpStatus determineStatus(Throwable ex) {
        return (ex instanceof HttpStatusCodeException)
                ? ((HttpStatusCodeException) ex).getStatusCode()
                : HttpStatus.INTERNAL_SERVER_ERROR;
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

    private String getRequestURI(WebRequest request) {
        if (request instanceof ServletWebRequest) {
            HttpServletRequest requestHttp = ((ServletWebRequest) request).getRequest();
            return String.format("%s %s", requestHttp.getMethod(), requestHttp.getRequestURI());
        } else {
            return "";
        }
    }
}
