package ru.pavbatol.myplace.gateway.app.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {
    private T data;
    private ApiError error;
    private HttpStatus status;

    public static <T> ApiResponse<T> success(T data, HttpStatus status) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setData(data);
        response.setStatus(status);
        return response;
    }

    public static <T> ApiResponse<T> error(ApiError error, HttpStatus status) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setError(error);
        response.setStatus(status);
        return response;
    }

    public static <T> ApiResponse<T> status(HttpStatus status) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setStatus(status);
        return response;
    }
}
