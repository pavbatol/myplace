package ru.pavbatol.myplace.gateway.app.exeption.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

@RestControllerAdvice
public class GlobalExceptionHandler {

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

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ApiResponse<Void>> handleGenericException(Throwable ex, WebRequest webRequest) {
        HttpStatus httpStatus = determineStatus(ex);
        ApiError apiError = createApiError(ex, webRequest, httpStatus);

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

    private HttpStatus determineStatus(Throwable ex) {
        return (ex instanceof HttpStatusCodeException)
                ? ((HttpStatusCodeException) ex).getStatusCode()
                : HttpStatus.INTERNAL_SERVER_ERROR;
    }


    private ApiError createApiError(Throwable ex, WebRequest webRequest, HttpStatus httpStatus) {
        return new ApiError(
                getRequestURI(webRequest),
                httpStatus.toString(),
                (ex.getCause() == null) ? null : ex.getCause().getMessage(),
                ex.getMessage(),
                null,
                null,
                null);
    }
}
