package ru.pavbatol.myplace.profile.dto;

import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Value
public class ProfileDtoCreateRequest {
    @NotNull
    Long userId;

    @Email
    @NotNull
    String email;
}
