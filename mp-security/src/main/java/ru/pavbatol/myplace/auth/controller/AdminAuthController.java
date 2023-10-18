package ru.pavbatol.myplace.auth.controller;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.pavbatol.myplace.auth.service.AuthService;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("admin/auth")
@Tag(name = "Admin: Auth", description = "API for working with authorization")
public class AdminAuthController {

    private final AuthService authService;

    @DeleteMapping("users/{userUuid}/refresh-tokens")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "removeRefreshTokensByUserUuid", description = "deleting all refresh tokens for the user")
    public ResponseEntity<Void> removeRefreshTokensByUserUuid(@PathVariable(value = "userUuid") UUID userUuid) {
        log.debug("DELETE removeRefreshTokensByUserUuid() with userUuid: {}", userUuid);
        authService.removeRefreshTokensByUserUuid(userUuid);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("users/{userUuid}/access-tokens")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "removeAccessTokensByUserUuid", description = "deleting all access tokens for the user")
    public ResponseEntity<Void> removeAccessTokensByUserUuid(@PathVariable(value = "userUuid") UUID userUuid) {
        log.debug("DELETE removeAccessTokensByUserUuid() with userUuid: {}", userUuid);
        authService.removeAccessTokensByUserUuid(userUuid);
        return ResponseEntity.noContent().build();
    }

    // TODO: 18.10.2023 implement the method

//    @DeleteMapping("/tokens")
//    @SecurityRequirement(name = "JWT")
//    @Operation(summary = "removeAllRefreshTokens", description = "deleting all refresh tokens")
//    public ResponseEntity<Void> removeAllRefreshTokens() {
//        log.debug("DELETE removeAllRefreshTokens()");
//        authService.removeAllRefreshTokens();
//        return ResponseEntity.noContent().build();
//    }

    @GetMapping("/secret")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "printRandomSecrets", description = "printing random twu secret strings")
    public String printRandomSecrets() {
        log.debug("GET printRandomSecrets()");
        return String.format("%s\n\n%s",
                Encoders.BASE64.encode(Keys.secretKeyFor(SignatureAlgorithm.HS512).getEncoded()),
                Encoders.BASE64.encode(Keys.secretKeyFor(SignatureAlgorithm.HS512).getEncoded()));
    }
}
