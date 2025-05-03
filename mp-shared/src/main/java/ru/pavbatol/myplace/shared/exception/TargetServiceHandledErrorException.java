package ru.pavbatol.myplace.shared.exception;

import lombok.Getter;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import ru.pavbatol.myplace.shared.dto.api.ApiError;

@Getter
public class TargetServiceHandledErrorException extends TargetServiceErrorException {
    public TargetServiceHandledErrorException(@NonNull ApiError error, @NonNull HttpStatus status) {
        super(error, status);
    }

    public TargetServiceHandledErrorException(@NonNull String message, @NonNull HttpStatus status) {
        super(message, status);
    }
}
