package ru.pavbatol.myplace.security.user.model;

import lombok.Value;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;
import java.util.UUID;

@Value
public class UserAuthenticationPrincipal implements AuthenticatedPrincipal {
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
