package ru.pavbatol.myplace.user.dto;

import lombok.Value;

import java.io.Serializable;

@Value
public class UserDtoUnverified implements Serializable {
    String email;
    String login;
    String password;
    String code;
}
