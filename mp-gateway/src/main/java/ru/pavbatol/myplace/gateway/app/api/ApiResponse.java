package ru.pavbatol.myplace.gateway.app.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;
import ru.pavbatol.myplace.shared.dto.api.ApiError;

/**
 * A generic wrapper for standardized API responses, including success/error data and HTTP status.
 * Designed to be returned as {@link org.springframework.http.ResponseEntity<ApiResponse<T>>} in Spring controllers.
 *
 * <p><strong>Structure:</strong>
 * <ul>
 *   <li>{@code status} – HTTP status code (e.g., 200 OK, 404 Not Found)</li>
 *   <li>{@code data} – Payload of type {@code T}</li>
 *   <li>{@code error} – Details of an error (nullable, populated on failures or if needed)</li>
 * </ul>
 *
 * <p><strong>Common usage:</strong>
 * <pre>{@code
 * // Success with payload
 * return ResponseEntity.ok(ApiResponse.success(user, HttpStatus.OK));
 *
 * // Error
 * return ResponseEntity.badRequest()
 *     .body(ApiResponse.error(new ApiError("Invalid input"), HttpStatus.BAD_REQUEST));
 *
 * // Empty response (e.g., for DELETE)
 * return ResponseEntity.status(HttpStatus.NO_CONTENT)
 *     .body(ApiResponse.status(HttpStatus.NO_CONTENT));
 * }</pre>
 *
 * @param <T> Type of the response payload
 * @see org.springframework.http.ResponseEntity
 * @see HttpStatus
 * @see ApiError
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {
    private HttpStatus status;
    private T data;
    private ApiError error;

    /**
     * Creates a success response with payload and HTTP status.
     *
     * @param data   Response payload (e.g., DTO, entity)
     * @param status HTTP status (typically 2xx)
     * @return {@link ApiResponse} with populated data
     */
    public static <T> ApiResponse<T> success(@NonNull T data, @NonNull HttpStatus status) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setData(data);
        response.setStatus(status);
        return response;
    }

    /**
     * Creates an error response with {@link ApiError} details and HTTP status.
     *
     * @param error  Error details (message, code, etc.)
     * @param status HTTP status (e.g., 4xx or 5xx)
     * @return {@link ApiResponse} with populated error
     */
    public static <T> ApiResponse<T> error(@NonNull ApiError error, @NonNull HttpStatus status) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setError(error);
        response.setStatus(status);
        return response;
    }

    /**
     * Creates a response with only an HTTP status (no payload or error).
     * Useful for empty responses like {@code 204 No Content}.
     *
     * @param status HTTP status
     * @return {@link ApiResponse} with only status set
     */
    public static <T> ApiResponse<T> status(@NonNull HttpStatus status) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setStatus(status);
        return response;
    }
}
