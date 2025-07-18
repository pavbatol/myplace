package ru.pavbatol.myplace.security.permission.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.pavbatol.myplace.security.permission.dto.UserIdentityDto;
import ru.pavbatol.myplace.security.permission.service.PermissionService;

import javax.validation.constraints.NotEmpty;
import java.util.List;

import static ru.pavbatol.myplace.shared.constant.HttpHeaders.X_USER_ID;
import static ru.pavbatol.myplace.shared.constant.HttpHeaders.X_USER_UUID;

/**
 * REST controller for handling permission and access control validation.
 * This controller provides an endpoint to verify if an authenticated user
 * possesses the required roles to access certain resources.
 *
 * <p>The controller requires a valid JWT token in the Authorization header
 * and validates the user's roles against the required roles specified in the request.</p>
 *
 * @apiNote All endpoints are prefixed with the value from `${api.prefix}` property
 * @SecurityRequirement JWT (required for all operations)
 * @Tag This controller is marked as "Permission: Private" in API documentation,
 * indicating it handles sensitive access control operations
 * @see PermissionService
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/permission")
@Tag(name = "Permission: Private", description = "API for access control validation")
public class PermissionController {
    private final PermissionService service;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/check-access")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "checkAccess", description = "Check if user has required roles, returns id/uuid if required")
    public ResponseEntity<Void> checkAccess(@RequestBody @NotEmpty List<String> requiredRoles,
                                            @RequestHeader("Authorization") String bearerToken,
                                            @RequestParam(value = "includeUserId", defaultValue = "false") boolean shouldReturnUserId,
                                            @RequestParam(value = "includeUserUuid", defaultValue = "false") boolean shouldReturnUserUuid) {

        log.debug("POST checkAccess() with requiredRoles: {}, bearerToken: [hidden], includeUserId: {}, includeUserUuid: {}",
                requiredRoles, shouldReturnUserId, shouldReturnUserUuid);

        UserIdentityDto userIdentityDto = service.validateAccess(requiredRoles, bearerToken, shouldReturnUserId, shouldReturnUserUuid);

        HttpHeaders headers = new HttpHeaders();
        if (shouldReturnUserId && userIdentityDto.getUserId() != null) {
            headers.set(X_USER_ID, String.valueOf(userIdentityDto.getUserId()));
        }
        if (shouldReturnUserUuid && userIdentityDto.getUserUuid() != null) {
            headers.set(X_USER_UUID, userIdentityDto.getUserUuid().toString());
        }

        return ResponseEntity.ok().headers(headers).build();
    }
}
