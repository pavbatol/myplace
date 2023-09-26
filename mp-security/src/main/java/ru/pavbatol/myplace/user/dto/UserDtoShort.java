package ru.pavbatol.myplace.user.dto;

import lombok.Value;
import ru.pavbatol.myplace.role.dto.RoleDto;

import java.time.LocalDateTime;
import java.util.UUID;

@Value
public class UserDtoShort {
    UUID uuid;
    String login;
}
