package ru.pavbatol.myplace.user.dto;

import lombok.Value;

@Value
public class UserDtoConfirm {
    String email;
    String code;
}
