package ru.pavbatol.myplace.shared.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import ru.pavbatol.myplace.shared.dto.api.ErrorResponse;

@Getter
public class ApiErrorException extends RuntimeException {
    private final ErrorResponse error;
    private final HttpStatus status;

    public ApiErrorException(ErrorResponse error, HttpStatus status) {
        super(error.getMessage());
        this.error = error;
        this.status = status;
    }

    public ApiErrorException(String message, HttpStatus status) {
        super(message);
        this.error = new ErrorResponse(
                null,
                null,
                null,
                message,
                null,
                null,
                null
        );
        this.status = status;
    }

    public String getFullError() {
        return String.format("Status: %s, Error: %s", status, error);
    }
}
