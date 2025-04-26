package ru.pavbatol.myplace.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.pavbatol.myplace.auth.service.AuthService;
import ru.pavbatol.myplace.shared.dto.security.auth.AuthDtoRefreshRequest;
import ru.pavbatol.myplace.shared.dto.security.auth.AuthDtoResponse;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/users/auth")
@Tag(name = "Private: Auth", description = "API for working with authorization")
public class PrivateAuthController {

    private final AuthService authService;

    @PostMapping("/refresh-tokens")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "getNewRefreshToken", description = "getting a new refresh token to replace the old one")
    public ResponseEntity<AuthDtoResponse> getNewRefreshToken(HttpServletRequest request,
                                                              @RequestBody AuthDtoRefreshRequest dtoRefreshRequest) {
        log.debug("POST getNewRefreshToken() with refreshToken: hidden for security");
        AuthDtoResponse body = authService.getNewRefreshToken(request, dtoRefreshRequest.getRefreshToken());
        return ResponseEntity.ok(body);
    }

    @PostMapping("/logout/all")
    @Operation(summary = "logoutAllSessions", description = "log out on all devices")
    public ResponseEntity<String> logoutAllSessions(HttpServletRequest request) {
        log.debug("POST logoutAllSessions()");
        authService.logoutAllSessions(request);
        return ResponseEntity.ok("Logout successful");
    }
}
