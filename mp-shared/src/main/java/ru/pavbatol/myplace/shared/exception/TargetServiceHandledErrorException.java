package ru.pavbatol.myplace.shared.exception;

import lombok.Getter;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import ru.pavbatol.myplace.shared.dto.api.ErrorResponse;

@Getter
public class TargetServiceHandledErrorException extends RuntimeException {
    private final ErrorResponse error;
    private final HttpStatus status;

    public TargetServiceHandledErrorException(@NonNull ErrorResponse error, @NonNull HttpStatus status) {
        super(error.getMessage());
        this.error = error;
        this.status = status;
    }

    public TargetServiceHandledErrorException(@NonNull String message, @NonNull HttpStatus status) {
        super(message);
        this.error = new ErrorResponse(
                null,
                null,
                null,
                message,
                null,
                null,
                null
        );
        this.status = status;
    }

    public String getFullError() {
        return String.format("Status: %s, Error: %s", status, error);
    }
}
