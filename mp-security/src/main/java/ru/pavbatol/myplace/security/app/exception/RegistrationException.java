package ru.pavbatol.myplace.security.app.exception;

public class RegistrationException extends AbstractRuntimeException {

    private static final String REASON = "Action failed.";

    public RegistrationException(String message) {
        super(message, REASON);
    }

    public RegistrationException(String message, String reason) {
        super(message, reason);
    }
}
