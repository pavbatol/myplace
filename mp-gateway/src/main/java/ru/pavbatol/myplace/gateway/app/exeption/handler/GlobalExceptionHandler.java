package ru.pavbatol.myplace.gateway.app.exeption.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import ru.pavbatol.myplace.gateway.app.api.ApiError;
import ru.pavbatol.myplace.gateway.app.api.ApiResponse;
import ru.pavbatol.myplace.gateway.app.exeption.ApiResponseException;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiResponseException.class)
    public ResponseEntity<ApiResponse<Void>> handleApiResponseException(ApiResponseException ex, WebRequest webRequest) {
        HttpStatus httpStatus = ex.getStatus();

        ApiError apiError = new ApiError(
                getRequestURI(webRequest),
                httpStatus.toString(),
                (ex.getCause() == null) ? null : ex.getCause().getMessage(),
                ex.getMessage(),
                null,
                null,
                null);

        ApiResponse<Void> apiResponse = ApiResponse.error(apiError, httpStatus);

        return ResponseEntity.status(httpStatus).body(apiResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGenericException(Exception ex, HttpStatus httpStatus, WebRequest webRequest) {
        ApiError apiError = new ApiError(
                getRequestURI(webRequest),
                httpStatus.toString(),
                (ex.getCause() == null) ? null : ex.getCause().getMessage(),
                ex.getMessage(),
                null,
                null,
                null);

        ApiResponse<Void> apiResponse = ApiResponse.error(apiError, httpStatus);

        return ResponseEntity.status(httpStatus).body(apiResponse);
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
