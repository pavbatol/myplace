package ru.pavbatol.myplace.app.exception;

import lombok.Getter;

@Getter
public abstract class AbstractRuntimeException extends RuntimeException {

    private final String reason;

    public AbstractRuntimeException(String message, String reason) {
        super(message);
        this.reason = reason;
    }
}