package ru.pavbatol.myplace.shared.dto.security.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Value
public class UserDtoUpdatePassword {
    @NotNull
    @Pattern(regexp = "^(?=\\S)(?!.*\\s$).{6,}$",
            message = "The string must not start/end with spaces and must contain at least 6 characters of any type")
    String password;

    @JsonCreator
    public UserDtoUpdatePassword(@JsonProperty("password") String password) {
        this.password = password;
    }
}
