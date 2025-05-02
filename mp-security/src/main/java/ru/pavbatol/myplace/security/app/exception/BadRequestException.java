package ru.pavbatol.myplace.security.app.exception;

public class BadRequestException extends AbstractRuntimeException {
    private static final String REASON = "Unsuitable request.";

    public BadRequestException(String message) {
        super(message, REASON);
    }

    public BadRequestException(String message, String reason) {
        super(message, reason);
    }
}