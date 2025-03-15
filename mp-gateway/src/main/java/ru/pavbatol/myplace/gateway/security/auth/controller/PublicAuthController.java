package ru.pavbatol.myplace.gateway.security.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.pavbatol.myplace.gateway.app.api.ApiResponse;
import ru.pavbatol.myplace.gateway.security.client.SecurityClient;
import ru.pavbatol.myplace.shared.dto.security.auth.AuthDtoRefreshRequest;
import ru.pavbatol.myplace.shared.dto.security.auth.AuthDtoRequest;
import ru.pavbatol.myplace.shared.dto.security.auth.AuthDtoResponse;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/${app.mp.security.label}/auth")
@Tag(name = "Public: Auth", description = "API for working with authorization")
public class PublicAuthController {

    private final SecurityClient client;

    @PostMapping("/logout")
    @Operation(summary = "logout", description = "log out on the current")
    public ResponseEntity<ApiResponse<String>> logout(HttpServletRequest request) {
        log.debug("POST logout()");
        return client.logout(request);
    }

    @PostMapping("/login")
    @Operation(summary = "login", description = "checking login and password and provide access and refresh tokens")
    public ResponseEntity<ApiResponse<AuthDtoResponse>> login(HttpServletRequest request, @Valid @RequestBody AuthDtoRequest dtoAuthRequest) {
        log.debug("POST login() with login: {}, password: hidden for security", dtoAuthRequest.getLogin());
        return client.login(request, dtoAuthRequest);
    }

    @PostMapping("/tokens")
    @Operation(summary = "getNewAccessToken", description = "getting a new access token to replace the old one")
    public ResponseEntity<ApiResponse<AuthDtoResponse>> getNewAccessToken(HttpServletRequest request,
                                                                          @Valid @RequestBody AuthDtoRefreshRequest dtoRefreshRequest) {
        log.debug("POST getNewAccessToken() with refreshToken: hidden for security");
        return client.getNewAccessToken(request, dtoRefreshRequest);
    }
}
