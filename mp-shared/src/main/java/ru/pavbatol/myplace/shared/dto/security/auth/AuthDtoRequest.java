package ru.pavbatol.myplace.shared.dto.security.auth;

import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Value
public class AuthDtoRequest {

    @NotBlank
    @Size(min = 2)
    String login;

    @NotBlank
    @Size(min = 6)
    String password;
}
