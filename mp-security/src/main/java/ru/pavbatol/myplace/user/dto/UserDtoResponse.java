package ru.pavbatol.myplace.user.dto;

import lombok.Value;
import ru.pavbatol.myplace.shared.dto.security.role.RoleDto;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Value
public class UserDtoResponse {
    UUID uuid;
    String login;
    Boolean deleted;
    Set<RoleDto> roles = new HashSet<>();
}
