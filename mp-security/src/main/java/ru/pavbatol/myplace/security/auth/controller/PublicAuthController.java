package ru.pavbatol.myplace.security.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.pavbatol.myplace.security.auth.service.AuthService;
import ru.pavbatol.myplace.shared.dto.security.auth.AuthDtoRefreshRequest;
import ru.pavbatol.myplace.shared.dto.security.auth.AuthDtoRequest;
import ru.pavbatol.myplace.shared.dto.security.auth.AuthDtoResponse;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/auth")
@Tag(name = "Public: Auth", description = "API for working with authorization")
public class PublicAuthController {

    private final AuthService authService;

    @PostMapping("/logout")
    @Operation(summary = "logout", description = "log out on the current")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        log.debug("POST logout()");
        authService.logout(request);
        return ResponseEntity.ok("Logout successful");
    }

    @PostMapping("/login")
    @Operation(summary = "login", description = "checking login and password and provide access and refresh tokens")
    public ResponseEntity<AuthDtoResponse> login(HttpServletRequest request, @RequestBody AuthDtoRequest dtoAuthRequest) {
        log.debug("POST login() with login: {}, password: hidden for security", dtoAuthRequest.getLogin());
        AuthDtoResponse dtoAuthResponse = authService.login(request, dtoAuthRequest);
        return ResponseEntity.ok(dtoAuthResponse);
    }

    @PostMapping("/tokens")
    @Operation(summary = "getNewAccessToken", description = "getting a new access token to replace the old one")
    public ResponseEntity<AuthDtoResponse> getNewAccessToken(HttpServletRequest request,
                                                             @RequestBody AuthDtoRefreshRequest dtoRefreshRequest) {
        log.debug("POST getNewAccessToken() with refreshToken: hidden for security");
        AuthDtoResponse body = authService.getNewAccessToken(request, dtoRefreshRequest.getRefreshToken());
        return ResponseEntity.ok(body);
    }
}
