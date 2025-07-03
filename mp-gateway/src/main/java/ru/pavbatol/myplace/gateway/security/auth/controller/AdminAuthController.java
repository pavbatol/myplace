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
import ru.pavbatol.myplace.gateway.security.auth.service.SecurityAuthService;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * Administrative controller for managing authentication tokens and sessions.
 * Provides endpoints for revoking user tokens (access/refresh) and clearing authentication storage.
 *
 * <p><b>Security:</b> All endpoints require JWT authentication with admin privileges.</p>
 * <p><b>Path:</b> {@code ${api.prefix}/${app.mp.security.label}/admin/auth}</p>
 *
 * @see SecurityAuthService
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/${app.mp.security.label}/admin/auth")
@Tag(name = "[Security]Auth: Admin", description = "API for working with authorization")
public class AdminAuthController {

    private final SecurityAuthService service;

    @DeleteMapping("users/{userUuid}/refresh-tokens")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "removeRefreshTokensByUserUuid", description = "deleting all refresh tokens for the user")
    public ResponseEntity<ApiResponse<Void>> removeRefreshTokensByUserUuid(HttpServletRequest servletRequest,
                                                                           @PathVariable(value = "userUuid") UUID userUuid) {
        log.debug("DELETE removeRefreshTokensByUserUuid() with userUuid: {}", userUuid);
        ApiResponse<Void> apiResponse = service.removeRefreshTokensByUserUuid(userUuid, HttpUtils.extractHeaders(servletRequest));
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    @DeleteMapping("users/{userUuid}/access-tokens")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "removeAccessTokensByUserUuid", description = "deleting all access tokens for the user")
    public ResponseEntity<ApiResponse<Void>> removeAccessTokensByUserUuid(HttpServletRequest servletRequest,
                                                                          @PathVariable(value = "userUuid") UUID userUuid) {
        log.debug("DELETE removeAccessTokensByUserUuid() with userUuid: {}", userUuid);
        ApiResponse<Void> apiResponse = service.removeAccessTokensByUserUuid(userUuid, HttpUtils.extractHeaders(servletRequest));
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    @DeleteMapping("/tokens")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "clearAuthStorage",
            description = "deleting all tokens and unverified logins and emails, existing users are not deleted")
    public ResponseEntity<ApiResponse<Void>> clearAuthStorage(HttpServletRequest servletRequest) {
        log.debug("DELETE clearAuthStorage()");
        ApiResponse<Void> apiResponse = service.clearAuthStorage(HttpUtils.extractHeaders(servletRequest));
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }
}
