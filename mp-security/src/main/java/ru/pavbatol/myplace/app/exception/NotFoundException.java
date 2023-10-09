package ru.pavbatol.myplace.app.exception;

public class NotFoundException extends AbstractRuntimeException {

    private static final String REASON = "Data not found.";

    public NotFoundException(String message) {
        super(message, REASON);
    }

    public NotFoundException(String message, String reason) {
        super(message, reason);
    }
}
