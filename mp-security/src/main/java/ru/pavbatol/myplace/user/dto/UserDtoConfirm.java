package ru.pavbatol.myplace.user.dto;

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
