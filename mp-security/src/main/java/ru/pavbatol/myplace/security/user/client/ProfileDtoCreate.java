package ru.pavbatol.myplace.security.user.client;

import lombok.Value;

@Value
public class ProfileDtoCreate {
    Long userId;
    String email;
}
