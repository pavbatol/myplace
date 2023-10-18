package ru.pavbatol.myplace.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.pavbatol.myplace.auth.dto.AuthDtoRefreshRequest;
import ru.pavbatol.myplace.auth.dto.AuthDtoRequest;
import ru.pavbatol.myplace.auth.dto.AuthDtoResponse;
import ru.pavbatol.myplace.auth.service.AuthService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "Public: Auth", description = "API for working with authorization")
public class PublicAuthController {

    private final AuthService authService;

    @GetMapping("/logout")
    @Operation(summary = "logout", description = "de - logging (let the frontend not forget to delete the access token)")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        log.debug("GET logout()");
        authService.logout(request);
        return ResponseEntity.ok("Logout successful");
    }

    @PostMapping("/login")
    @Operation(summary = "login", description = "checking login and password and provide access and refresh tokens")
    public ResponseEntity<AuthDtoResponse> login(HttpServletRequest request, @Valid @RequestBody AuthDtoRequest dtoAuthRequest) {
        log.debug("POST login() with login: {}, password: hidden for security", dtoAuthRequest.getLogin());
        AuthDtoResponse dtoAuthResponse = authService.login(request, dtoAuthRequest);
        return ResponseEntity.ok(dtoAuthResponse);
    }

    @PostMapping("/token")
    @Operation(summary = "getNewAccessToken", description = "getting a new access token to replace the old one")
    public ResponseEntity<AuthDtoResponse> getNewAccessToken(HttpServletRequest request,
                                                             @Valid @RequestBody AuthDtoRefreshRequest dtoRefreshRequest) {
        log.debug("POST getNewAccessToken() with refreshToken: hidden for security");
        AuthDtoResponse body = authService.getNewAccessToken(request, dtoRefreshRequest.getRefreshToken());
        return ResponseEntity.ok(body);
    }
}
