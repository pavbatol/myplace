package ru.pavbatol.myplace.shared.exception;

import lombok.Getter;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import ru.pavbatol.myplace.shared.dto.api.ApiError;

@Getter
public class TargetServiceErrorException extends RuntimeException {
    private final ApiError error;
    private final HttpStatus status;

    public TargetServiceErrorException(@NonNull ApiError error, @NonNull HttpStatus status) {
        super(error.getMessage());
        this.error = error;
        this.status = status;
    }

    public TargetServiceErrorException(@NonNull String message, @NonNull HttpStatus status) {
        super(message);
        this.error = ApiError.message(message);
        this.status = status;
    }

    public String getFullError() {
        return String.format("Status: %s, Error: %s", status, error);
    }
}
