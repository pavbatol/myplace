package ru.pavbatol.myplace.security.permission.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.pavbatol.myplace.security.permission.service.PermissionService;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * REST controller for handling permission and access control validation.
 * This controller provides an endpoint to verify if an authenticated user
 * possesses the required roles to access certain resources.
 *
 * <p>The controller requires a valid JWT token in the Authorization header
 * and validates the user's roles against the required roles specified in the request.</p>
 *
 * @see PermissionService
 * @apiNote All endpoints are prefixed with the value from `${api.prefix}` property
 *
 * @SecurityRequirement JWT (required for all operations)
 * @Tag This controller is marked as "Permission: Private" in API documentation,
 *      indicating it handles sensitive access control operations
 */
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
    @Operation(summary = "checkAccess", description = "Check if user has required roles")
    public ResponseEntity<Void> checkAccess(@RequestBody @NotEmpty List<String> requiredRoles,
                                            @RequestHeader("Authorization") String bearerToken) {

        service.validateAccess(requiredRoles, bearerToken);
        return ResponseEntity.ok().build();
    }
}
