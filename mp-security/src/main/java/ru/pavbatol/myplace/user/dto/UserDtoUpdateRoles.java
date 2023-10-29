package ru.pavbatol.myplace.user.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Value
public class UserDtoUpdateRoles {
    @NotNull
    Set<Long> roleIds;

    @JsonCreator
    public UserDtoUpdateRoles(@JsonProperty("roleIds") Set<Long> roleIds) {
        this.roleIds = roleIds;
    }
}
