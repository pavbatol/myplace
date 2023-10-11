package ru.pavbatol.myplace.user.model;

import lombok.Value;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;
import java.util.UUID;

@Value
public class UserAuthenticatedPrincipal implements AuthenticatedPrincipal {
    Long id;
    UUID uuid;
    String login;
    Set<GrantedAuthority> authorities;
    boolean enabled;

    @Override
    public String getName() {
        return login;
    }
}
