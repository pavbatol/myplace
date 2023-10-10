package ru.pavbatol.myplace.jwt;

import lombok.*;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import ru.pavbatol.myplace.user.model.UserAuthenticatedPrincipal;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class JwtAuthentication implements Authentication {
    private UserAuthenticatedPrincipal principal;
    private Set<GrantedAuthority> authorities;
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private Boolean authenticated;

    public static JwtAuthentication of(UserAuthenticatedPrincipal principal,
                                       Collection<? extends GrantedAuthority> authorities,
                                       boolean authenticated) {
        JwtAuthentication jwtAuthentication = new JwtAuthentication();
        jwtAuthentication.setAuthenticated(authenticated);
        jwtAuthentication.setPrincipal(principal);
        jwtAuthentication.setAuthorities(new HashSet<>(authorities));
        return jwtAuthentication;
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
        return authenticated != null && authenticated;
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
