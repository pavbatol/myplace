package ru.pavbatol.myplace.gateway.security.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.pavbatol.myplace.gateway.app.api.ApiResponse;
import ru.pavbatol.myplace.gateway.app.util.HttpUtils;
import ru.pavbatol.myplace.gateway.security.auth.client.SecurityAuthClient;
import ru.pavbatol.myplace.shared.dto.security.auth.AuthDtoRefreshRequest;
import ru.pavbatol.myplace.shared.dto.security.auth.AuthDtoResponse;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/${app.mp.security.label}/users/auth")
@Tag(name = "Private: Auth", description = "API for working with authorization")
public class PrivateAuthController {

    private final SecurityAuthClient client;

    @PostMapping("/refresh-tokens")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "getNewRefreshToken", description = "getting a new refresh token to replace the old one")
    public ResponseEntity<ApiResponse<AuthDtoResponse>> getNewRefreshToken(HttpServletRequest servletRequest,
                                                                           @Valid @RequestBody AuthDtoRefreshRequest dtoRefreshRequest) {
        log.debug("POST getNewRefreshToken() with refreshToken: hidden for security");
        return client.getNewRefreshToken(dtoRefreshRequest, HttpUtils.extractHeaders(servletRequest));
    }

    @PostMapping("/logout/all")
    @Operation(summary = "logoutAllSessions", description = "log out on all devices")
    public ResponseEntity<ApiResponse<String>> logoutAllSessions(HttpServletRequest servletRequest) {
        log.debug("POST logoutAllSessions()");
        return client.logoutAllSessions(HttpUtils.extractHeaders(servletRequest));
    }
}
