package ru.pavbatol.myplace.app.exception;

import lombok.Getter;

@Getter
public class AbstractBaseRuntimeException extends RuntimeException {

    private final String reason;

    public AbstractBaseRuntimeException(String message, String reason) {
        super(message);
        this.reason = reason;
    }
}