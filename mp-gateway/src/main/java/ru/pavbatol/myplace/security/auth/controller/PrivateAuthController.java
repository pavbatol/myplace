package ru.pavbatol.myplace.security.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.pavbatol.myplace.auth.dto.AuthDtoRefreshRequest;
import ru.pavbatol.myplace.auth.dto.AuthDtoResponse;
import ru.pavbatol.myplace.auth.service.AuthService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/auth")
@Tag(name = "Private: Auth", description = "API for working with authorization")
public class PrivateAuthController {

    private final AuthService authService;

    @PostMapping("/refresh-tokens")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "getNewRefreshToken", description = "getting a new refresh token to replace the old one")
    public ResponseEntity<AuthDtoResponse> getNewRefreshToken(HttpServletRequest request,
                                                              @Valid @RequestBody AuthDtoRefreshRequest dtoRefreshRequest) {
        log.debug("POST getNewRefreshToken() with refreshToken: hidden for security");
        AuthDtoResponse body = authService.getNewRefreshToken(request, dtoRefreshRequest.getRefreshToken());
        return ResponseEntity.ok(body);
    }

    @GetMapping("/logout/all")
    @Operation(summary = "logoutAllSessions", description = "log out on all devices")
    public ResponseEntity<String> logoutAllSessions(HttpServletRequest request) {
        log.debug("GET logoutAllSessions()");
        authService.logoutAllSessions(request);
        return ResponseEntity.ok("Logout successful");
    }
}
