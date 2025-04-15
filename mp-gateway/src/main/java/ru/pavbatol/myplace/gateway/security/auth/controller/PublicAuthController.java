package ru.pavbatol.myplace.gateway.security.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.pavbatol.myplace.gateway.app.api.ApiResponse;
import ru.pavbatol.myplace.gateway.app.util.HttpUtils;
import ru.pavbatol.myplace.gateway.security.auth.service.SecurityAuthService;
import ru.pavbatol.myplace.shared.dto.security.auth.AuthDtoRefreshRequest;
import ru.pavbatol.myplace.shared.dto.security.auth.AuthDtoRequest;
import ru.pavbatol.myplace.shared.dto.security.auth.AuthDtoResponse;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * Public authentication controller handling core security operations.
 * Provides unauthenticated endpoints for user login and token management.
 *
 * <p><b>Key operations:</b>
 * <ul>
 *   <li>User login with credentials</li>
 *   <li>Access token refresh</li>
 *   <li>Session logout</li>
 * </ul>
 *
 * <p><b>Security:</b> All endpoints are publicly accessible and do not require prior authentication.
 * The controller implements security measures for credential validation and token generation.</p>
 *
 * <p><b>Base Path:</b> {@code ${api.prefix}/${app.mp.security.label}/auth}</p>
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/${app.mp.security.label}/auth")
@Tag(name = "Public: Auth", description = "API for working with authorization")
public class PublicAuthController {

    private final SecurityAuthService service;

    @PostMapping("/logout")
    @Operation(summary = "logout", description = "log out on the current")
    public ResponseEntity<ApiResponse<String>> logout(HttpServletRequest servletRequest) {
        log.debug("POST logout()");
        ApiResponse<String> apiResponse = service.logout(HttpUtils.extractHeaders(servletRequest));
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    @PostMapping("/login")
    @Operation(summary = "login", description = "checking login and password and provide access and refresh tokens")
    public ResponseEntity<ApiResponse<AuthDtoResponse>> login(HttpServletRequest servletRequest,
                                                              @Valid @RequestBody AuthDtoRequest dtoAuthRequest) {
        log.debug("POST login() with login: {}, password: hidden for security", dtoAuthRequest.getLogin());
        ApiResponse<AuthDtoResponse> apiResponse = service.login(dtoAuthRequest, HttpUtils.extractHeaders(servletRequest));
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    @PostMapping("/tokens")
    @Operation(summary = "getNewAccessToken", description = "getting a new access token to replace the old one")
    public ResponseEntity<ApiResponse<AuthDtoResponse>> getNewAccessToken(HttpServletRequest servletRequest,
                                                                          @Valid @RequestBody AuthDtoRefreshRequest dtoRefreshRequest) {
        log.debug("POST getNewAccessToken() with refreshToken: hidden for security");
        ApiResponse<AuthDtoResponse> apiResponse = service.getNewAccessToken(dtoRefreshRequest, HttpUtils.extractHeaders(servletRequest));
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }
}
