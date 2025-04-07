package ru.pavbatol.myplace.user.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Value
public class UserDtoUpdatePassword_old {
    @NotNull
    @Pattern(regexp = "^(?=\\S)(?!.*\\s$).{6,}$",
            message = "The string must not start/end with spaces and must contain at least 6 characters of any type")
    String password;

    @JsonCreator
    public UserDtoUpdatePassword_old(@JsonProperty("password") String password) {
        this.password = password;
    }
}
