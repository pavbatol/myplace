package ru.pavbatol.myplace.security.permission.dto;

import lombok.Value;

import java.util.UUID;

@Value
public class UserIdentityDto {
    Long userId;
    UUID userUuid;
}
