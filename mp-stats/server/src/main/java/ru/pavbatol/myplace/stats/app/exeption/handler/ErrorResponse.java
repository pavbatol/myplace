package ru.pavbatol.myplace.stats.app.exeption.handler;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.time.OffsetDateTime;
import java.util.List;

@Value
public class ErrorResponse {
    @JsonProperty("offset-timestamp")
    OffsetDateTime timestamp = OffsetDateTime.now();

    @JsonProperty("status")
    int status;

    @JsonProperty("error")
    String error;

    @JsonProperty("reasons")
    List<String> reasons;
}