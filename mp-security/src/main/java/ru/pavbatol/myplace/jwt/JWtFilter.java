package ru.pavbatol.myplace.jwt;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.pavbatol.myplace.auth.service.AuthService;
import ru.pavbatol.myplace.user.model.UserAuthenticatedPrincipal;
import ru.pavbatol.myplace.user.service.UserDetailService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component(value = "JWtFilter")
@RequiredArgsConstructor
public class JWtFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final UserDetailService userDetailService;
    @Lazy
    @Autowired
    private AuthService authService;


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        log.debug("Trying to set authentication");
        jwtProvider.resolveToken(request)
                .filter(jwtProvider::validateAccessToken)
                .map(accessToken -> jwtProvider.getAccessClaims(accessToken).getSubject())
                .filter(login -> authService.checkAccessTokenExists(request, login))
                .map(login -> {
                    UserAuthenticatedPrincipal principal = userDetailService.loadUserByLogin(login);
                    return JwtAuthentication.of(principal, principal.getAuthorities(), principal.isEnabled());
                })
                .ifPresent(authentication -> {
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    log.debug("Authentication was set into SecurityContextHolder: {}",
                            SecurityContextHolder.getContext().getAuthentication().toString());
                });
        filterChain.doFilter(request, response);
    }
}
