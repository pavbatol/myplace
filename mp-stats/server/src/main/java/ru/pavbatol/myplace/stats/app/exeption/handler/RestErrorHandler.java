package ru.pavbatol.myplace.stats.app.exeption.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class RestErrorHandler {

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<Object> handleWebExchangeBindException(final WebExchangeBindException ex) {
        String message = "Failed exchanging";
        log.warn(message);
        return makeResponseEntity(message, ex, BAD_REQUEST);
    }

    @ExceptionHandler(Throwable.class)
    protected ResponseEntity<Object> handleThrowableEx(Throwable ex) {
        String message = "Unexpected error";
        return makeResponseEntity(message, ex, INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<Object> makeResponseEntity(String message,
                                                      Throwable ex,
                                                      HttpStatus status) {
        log.error(message + ": {}", ex.getMessage(), ex);
        ErrorResponse errorResponse = makeBody(message, status, ex);
        return new ResponseEntity<>(errorResponse, status);
    }

    private ErrorResponse makeBody(String message, HttpStatus status, Throwable ex) {
        List<String> reasons;
        if (ex instanceof BindingResult) {
            reasons = ((BindingResult) ex)
                    .getAllErrors().stream()
                    .map(this::getErrorString)
                    .collect(Collectors.toList());
        } else {
            reasons = Arrays.stream(ex.getMessage().split(", ")).collect(Collectors.toList());
        }
        return new ErrorResponse(
                status.value(),
                message,
                reasons);
    }

    private String getErrorString(ObjectError error) {
        if (error instanceof FieldError) {
            return ((FieldError) error).getField() + ' ' + error.getDefaultMessage();
        }
        return error.getDefaultMessage();
    }
}