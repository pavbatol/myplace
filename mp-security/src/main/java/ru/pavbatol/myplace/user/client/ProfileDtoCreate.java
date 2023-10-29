package ru.pavbatol.myplace.user.client;

import lombok.Value;

@Value
public class ProfileDtoCreate {
    Long userId;
    String email;
}
