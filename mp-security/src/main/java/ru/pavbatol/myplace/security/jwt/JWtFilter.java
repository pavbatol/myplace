package ru.pavbatol.myplace.security.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.pavbatol.myplace.security.auth.service.AuthService;
import ru.pavbatol.myplace.security.user.model.UserAuthenticationPrincipal;
import ru.pavbatol.myplace.security.user.service.UserDetailService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component(value = "JWtFilter")
@RequiredArgsConstructor
public class JWtFilter extends OncePerRequestFilter {
    @Value("${app.jwt.access.storing}")
    private boolean accessTokenStoring;
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
                .filter(accessToken -> !accessTokenStoring || authService.checkAccessTokenExists(request, accessToken))
                .map(accessToken -> jwtProvider.getAccessClaims(accessToken).getSubject())
                .map(login -> {
                    UserAuthenticationPrincipal principal = userDetailService.loadUserByLogin(login);
                    return JwtAuthentication.of(principal, principal.getAuthorities(), principal.isEnabled());
                })
                .ifPresentOrElse(
                        authentication -> {
                            SecurityContextHolder.getContext().setAuthentication(authentication);
                            log.debug("Authentication was set: {}",
                                    SecurityContextHolder.getContext().getAuthentication().toString());
                        },
                        () -> log.debug("Authentication was NOT set")
                );
        filterChain.doFilter(request, response);
    }
}
