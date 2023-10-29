package ru.pavbatol.myplace.role.dto;

import lombok.Value;
import ru.pavbatol.myplace.role.model.RoleName;

@Value
public class RoleDto {
    Long id;
    RoleName roleName;
}
