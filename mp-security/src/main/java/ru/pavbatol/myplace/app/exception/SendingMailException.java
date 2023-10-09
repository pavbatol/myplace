package ru.pavbatol.myplace.app.exception;

public class SendingMailException extends AbstractRuntimeException {

    private static final String REASON = "Mail error.";

    public SendingMailException(String message) {
        super(message, REASON);
    }

    public SendingMailException(String message, String reason) {
        super(message, reason);
    }
}
