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

import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/admin/auth")
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

    @DeleteMapping("/tokens")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "clearAuthStorage",
            description = "deleting all tokens and unverified logins and emails, existing users are not deleted")
    public ResponseEntity<Void> clearAuthStorage() {
        log.debug("DELETE clearAuthStorage()");
        authService.clearAuthStorage();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/secrets")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "printRandomSecrets", description = "printing random two secret strings")
    public String printRandomSecrets() {
        log.debug("GET printRandomSecrets()");
        return String.format("%s\n\n%s",
                Encoders.BASE64.encode(Keys.secretKeyFor(SignatureAlgorithm.HS512).getEncoded()),
                Encoders.BASE64.encode(Keys.secretKeyFor(SignatureAlgorithm.HS512).getEncoded()));
    }

    @GetMapping("/secrets/pair")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "printRandomPairSecrets", description = "printing random two secret strings (publicKey, privateKey)")
    public String printRandomPairSecrets() {
        log.debug("GET printRandomPairSecrets()");

        KeyPair keyPair = generateKeyPair();
        Key publicKey = keyPair.getPublic();
        Key privateKey = keyPair.getPrivate();

        return String.format("PUBLIC_KEY:%n%n%s\n\nPRIVATE_KEY:%n%n%s",
                Encoders.BASE64.encode(publicKey.getEncoded()),
                Encoders.BASE64.encode(privateKey.getEncoded()));
    }

    private static KeyPair generateKeyPair() {
        KeyPairGenerator keyPairGenerator = null;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
    }
}
