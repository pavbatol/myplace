package ru.pavbatol.myplace.app.exception;

public class ExternalServerException extends AbstractRuntimeException {

    private static final String REASON = "Unexpected response.";

    public ExternalServerException(String message) {
        super(message, REASON);
    }

    public ExternalServerException(String message, String reason) {
        super(message, reason);
    }
}
