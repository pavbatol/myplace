package ru.pavbatol.myplace.shared.exception;

import lombok.Getter;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import ru.pavbatol.myplace.shared.dto.api.ApiError;

@Getter
public class TargetServiceHandledErrorException extends RuntimeException {
    private final ApiError error;
    private final HttpStatus status;

    public TargetServiceHandledErrorException(@NonNull ApiError error, @NonNull HttpStatus status) {
        super(error.getMessage());
        this.error = error;
        this.status = status;
    }

    public TargetServiceHandledErrorException(@NonNull String message, @NonNull HttpStatus status) {
        super(message);
        this.error = new ApiError(
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
