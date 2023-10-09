package ru.pavbatol.myplace.user.dto;

import lombok.Value;

import javax.validation.constraints.*;

@Value
public class UserDtoRegistry {
    @NotBlank
    @Email
    String email;

    @NotNull
    @Pattern(regexp = "^(?=\\S)(?!.*\\s$).{2,}$",
            message = "The string must not start/end with spaces and must contain at least 2 characters of any type")
    String login;

    @NotNull
    @Pattern(regexp = "^(?=\\S)(?!.*\\s$).{6,}$",
            message = "The string must not start/end with spaces and must contain at least 6 characters of any type")
    String password;
}
