package ru.pavbatol.myplace.role.dto;

import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import ru.pavbatol.myplace.role.model.RoleName;

import javax.persistence.*;

@Value
public class RoleDto {
    Long id;
    RoleName roleName;
}
