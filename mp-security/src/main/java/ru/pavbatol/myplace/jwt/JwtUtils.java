package ru.pavbatol.myplace.jwt;

import io.jsonwebtoken.Claims;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import ru.pavbatol.myplace.role.model.Role;
import ru.pavbatol.myplace.role.model.RoleName;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JwtUtils {
//    public static Set<RoleName> toRoleNames(Collection<Role> roles) {
//        return roles.stream()
//                .map(Role::getRoleName)
//                .collect(Collectors.toSet());
//    }

//    public static Set<RoleName> getRoleNames(Claims claims) {
//        final List<String> roles = claims.get("roles", List.class);
//        return roles.stream()
//                .map(RoleName::of)
//                .collect(Collectors.toSet());
//    }
}
