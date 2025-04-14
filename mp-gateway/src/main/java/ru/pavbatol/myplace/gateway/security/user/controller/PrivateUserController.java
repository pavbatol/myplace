package ru.pavbatol.myplace.gateway.security.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.pavbatol.myplace.gateway.app.api.ApiResponse;
import ru.pavbatol.myplace.gateway.security.user.service.SecurityUserService;
import ru.pavbatol.myplace.shared.dto.security.user.UserDtoUpdatePassword;

import javax.validation.Valid;
import java.util.UUID;

/**
 * REST controller for authenticated user operations.
 * Provides endpoints for password management and user identifier lookup.
 *
 * <p>All endpoints require a valid JWT token in the request headers.
 *
 * <p>The base path is constructed from application properties:
 * ${api.prefix}/${app.mp.security.label}/users
 *
 * <p>Supported operations include:
 * <ul>
 *   <li>Changing a user's password (with proper validation)</li>
 *   <li>Retrieving a user's internal ID by their UUID</li>
 * </ul>
 *
 * <p>Security note: Password-related operations log requests without exposing sensitive data.
 *
 * @see SecurityUserService The underlying service handling business logic
 * @see UserDtoUpdatePassword The DTO used for password change operations
 */
@Slf4j
@RestController
@RequestMapping("${api.prefix}/${app.mp.security.label}/users")
@RequiredArgsConstructor
@Tag(name = "Private: User", description = "API for working with User")
public class PrivateUserController {
    private final SecurityUserService service;

    @PatchMapping("/{userUuid}/password")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "changePassword", description = "setting new password")
    public ResponseEntity<ApiResponse<Void>> changePassword(@PathVariable(value = "userUuid") UUID userUuid,
                                                            @Valid @RequestBody UserDtoUpdatePassword dto,
                                                            @RequestHeader HttpHeaders headers) {
        log.debug("POST changePassword() with userUuid: {}, dto: hidden for security", userUuid);
        ApiResponse<Void> apiResponse = service.changePassword(userUuid, dto, headers);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    @GetMapping("/{userUuid}/id")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "getIdByUuid", description = "obtaining User id by UUID")
    public ResponseEntity<ApiResponse<Long>> getIdByUuid(@PathVariable(value = "userUuid") UUID userUuid,
                                                         @RequestHeader HttpHeaders headers) {
        log.debug("POST getIdByUuid() with userUuid: {}", userUuid);
        ApiResponse<Long> apiResponse = service.getIdByUuid(userUuid, headers);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }
}
