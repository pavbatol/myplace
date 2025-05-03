package ru.pavbatol.myplace.gateway.app.exeption;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiResponseException extends RuntimeException {
    private final HttpStatus status;

    public ApiResponseException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public ApiResponseException(String message, HttpStatus status, Throwable cause) {
        super(message, cause);
        this.status = status;
    }
}