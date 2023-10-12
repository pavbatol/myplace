package ru.pavbatol.myplace.auth.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import javax.validation.constraints.NotBlank;

@Value
public class AuthDtoRefreshRequest {
    @NotBlank
    String refreshToken;

    @JsonCreator
    public AuthDtoRefreshRequest(@JsonProperty String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
