package ru.pavbatol.myplace.shared.dto.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Value
public class ApiError {
    LocalDateTime timestamp = LocalDateTime.now();

    @JsonInclude(NON_NULL)
    String mapping;

    String status;

    @JsonInclude(NON_NULL)
    String reason;

    String message;

    @JsonInclude(NON_NULL)
    String details;

    @JsonInclude(NON_NULL)
    List<String> errors;

    @JsonInclude(NON_NULL)
    List<String> trace;

    public static ApiError message(String message) {
        return new ApiError(null, null, null, message, null, null, null);
    }
}
