package ru.pavbatol.myplace.user.model;

import lombok.Data;
import org.springframework.security.core.AuthenticatedPrincipal;

import java.util.UUID;

@Data
public class UserAuthenticatedPrincipal implements AuthenticatedPrincipal {
    private final Long id;
    private final UUID uuid;
    private final String login;

    @Override
    public String getName() {
        return login;
    }
}
