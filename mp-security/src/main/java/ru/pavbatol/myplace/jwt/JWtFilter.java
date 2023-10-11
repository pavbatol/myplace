package ru.pavbatol.myplace.jwt;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.pavbatol.myplace.role.model.Role;
import ru.pavbatol.myplace.role.model.RoleName;
import ru.pavbatol.myplace.user.model.User;
import ru.pavbatol.myplace.user.model.UserAuthenticatedPrincipal;
import ru.pavbatol.myplace.user.service.UserService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class JWtFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        log.debug("Trying to set authentication");
        jwtProvider.resolveToken(request)
                .filter(jwtProvider::validateAccessToken)
                .map(accessToken -> {
                    Claims accessClaims = jwtProvider.getAccessClaims(accessToken);
                    String login = accessClaims.getSubject();
                    User user = userService.findByLogin(login);
                    UserAuthenticatedPrincipal principal = new UserAuthenticatedPrincipal(user.getId(), user.getUuid(), login);
                    List<RoleName> authorities = user.getRoles().stream()
                            .map(Role::getRoleName)
                            .collect(Collectors.toList());
                    return JwtAuthentication.of(principal, authorities, !user.getDeleted());
                })
                .ifPresent(authentication -> {
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    log.debug("Authentication was set into SecurityContextHolder: {}",
                            SecurityContextHolder.getContext().getAuthentication().toString());
                });
        filterChain.doFilter(request, response);
    }
}
