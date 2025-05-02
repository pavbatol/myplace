package ru.pavbatol.myplace.security.user.model;

import lombok.Value;

import java.io.Serializable;

@Value
public class UserUnverified implements Serializable {
    private static final long serialVersionUID = 100L;
    String email;
    String login;
    String password;
    String code;
}
