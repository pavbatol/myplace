package ru.pavbatol.myplace.shared.dto.security.user;

import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Value
public class UserDtoConfirm {
    @NotBlank
    @Email
    String email;

    @NotBlank
    String code;
}
