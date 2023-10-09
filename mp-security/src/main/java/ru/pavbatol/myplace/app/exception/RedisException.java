package ru.pavbatol.myplace.app.exception;

public class RedisException extends AbstractRuntimeException {

    private static final String REASON = "Unexpected result.";

    public RedisException(String message) {
        super(message, REASON);
    }

    public RedisException(String message, String reason) {
        super(message, reason);
    }
}
