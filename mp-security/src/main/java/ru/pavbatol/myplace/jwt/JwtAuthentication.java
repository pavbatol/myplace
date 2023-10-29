package ru.pavbatol.myplace.jwt;

import lombok.*;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import ru.pavbatol.myplace.user.model.UserAuthenticationPrincipal;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class JwtAuthentication implements Authentication {
    private UserAuthenticationPrincipal principal;
    private Set<GrantedAuthority> authorities;
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private boolean authenticated = false;

    public static JwtAuthentication of(UserAuthenticationPrincipal principal,
                                       Collection<? extends GrantedAuthority> authorities,
                                       boolean authenticated) {
        JwtAuthentication authentication = new JwtAuthentication();
        authentication.setPrincipal(principal);
        authentication.setAuthorities(new HashSet<>(authorities));
        authentication.setAuthenticated(authenticated);
        return authentication;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean authenticated) throws IllegalArgumentException {
        this.authenticated = authenticated;
    }

    @Override
    public String getName() {
        if (this.getPrincipal() instanceof AuthenticatedPrincipal) {
            return ((AuthenticatedPrincipal) this.getPrincipal()).getName();
        } else {
            return this.getPrincipal() == null ? "" : this.getPrincipal().toString();
        }
    }
}
