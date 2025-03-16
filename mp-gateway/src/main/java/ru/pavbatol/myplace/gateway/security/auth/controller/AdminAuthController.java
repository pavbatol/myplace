package ru.pavbatol.myplace.gateway.security.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.pavbatol.myplace.gateway.app.api.ApiResponse;
import ru.pavbatol.myplace.gateway.security.auth.client.SecurityAuthClient;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/${app.mp.security.label}/admin/auth")
@Tag(name = "Admin: Auth", description = "API for working with authorization")
public class AdminAuthController {

    private final SecurityAuthClient client;

    @DeleteMapping("users/{userUuid}/refresh-tokens")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "removeRefreshTokensByUserUuid", description = "deleting all refresh tokens for the user")
    public ResponseEntity<ApiResponse<Void>> removeRefreshTokensByUserUuid(@PathVariable(value = "userUuid") UUID userUuid) {
        log.debug("DELETE removeRefreshTokensByUserUuid() with userUuid: {}", userUuid);
        return client.removeRefreshTokensByUserUuid(userUuid);
    }

    @DeleteMapping("users/{userUuid}/access-tokens")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "removeAccessTokensByUserUuid", description = "deleting all access tokens for the user")
    public ResponseEntity<ApiResponse<Void>> removeAccessTokensByUserUuid(@PathVariable(value = "userUuid") UUID userUuid) {
        log.debug("DELETE removeAccessTokensByUserUuid() with userUuid: {}", userUuid);
        return client.removeAccessTokensByUserUuid(userUuid);
    }

    @DeleteMapping("/tokens")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "clearAuthStorage",
            description = "deleting all tokens and unverified logins and emails, existing users are not deleted")
    public ResponseEntity<ApiResponse<Void>> clearAuthStorage() {
        log.debug("DELETE clearAuthStorage()");
        return client.clearAuthStorage();
    }
}
