package ru.pavbatol.myplace.user.dto;

import lombok.Value;

@Value
public class UserDtoUnverified {
    String email;
    String login;
    String password;
    String code;
}
