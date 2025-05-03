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
import ru.pavbatol.myplace.shared.dto.security.user.UserDtoResponse;
import ru.pavbatol.myplace.shared.dto.security.user.UserDtoUpdateRoles;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

/**
 * REST controller for administrative user management operations.
 * Provides endpoints for managing user roles, soft-deleting users, and retrieving user information.
 *
 * <p>All endpoints require a valid JWT token in the request headers for authorization.
 * The base path is constructed from application properties: ${api.prefix}/${app.mp.security.label}/admin/users
 *
 * <p>This controller includes operations for:
 * <ul>
 *   <li>Updating user roles</li>
 *   <li>Soft-deleting users (marking as deleted)</li>
 *   <li>Retrieving individual users by UUID</li>
 *   <li>Fetching paginated lists of users</li>
 * </ul>
 *
 * @see SecurityUserService The underlying service that handles business logic
 * @see UserDtoResponse The DTO returned for user data
 * @see UserDtoUpdateRoles The DTO used for role update operations
 */
@Slf4j
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/${app.mp.security.label}/admin/users")
@Tag(name = "Admin: User", description = "API for working with users")
public class AdminUserController {

    private final SecurityUserService service;

    @PatchMapping("/{userUuid}/roles")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "updateRoles", description = "setting new role list")
    public ResponseEntity<ApiResponse<UserDtoResponse>> updateRoles(@PathVariable("userUuid") UUID userUuid,
                                                                    @RequestBody @Valid UserDtoUpdateRoles dto,
                                                                    @RequestHeader HttpHeaders headers) {
        log.debug("PUT updateRoles() with userUuid: {}, dto: {}", userUuid, dto);
        ApiResponse<UserDtoResponse> apiResponse = service.updateRoles(userUuid, dto, headers);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    @DeleteMapping("/{userUuid}")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "delete", description = "deleting a user (marks as deleted)")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable("userUuid") UUID userUuid,
                                                    @RequestHeader HttpHeaders headers) {
        log.debug("DELETE delete() with userUuid {}", userUuid);
        ApiResponse<Void> apiResponse = service.delete(userUuid, headers);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    @GetMapping("/{userUuid}")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "findByUuid", description = "getting a user by Id")
    public ResponseEntity<ApiResponse<UserDtoResponse>> findByUuid(@PathVariable("userUuid") UUID userUuid,
                                                                   @RequestHeader HttpHeaders headers) {
        log.debug("GET findByUuid() with userUuid {}", userUuid);
        ApiResponse<UserDtoResponse> apiResponse = service.findByUuid(userUuid, headers);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    @GetMapping
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "findAll", description = "getting users page by page")
    public ResponseEntity<ApiResponse<List<UserDtoResponse>>> findAll(@RequestParam(value = "from", defaultValue = "0") Integer from,
                                                                      @RequestParam(value = "size", defaultValue = "10") Integer size,
                                                                      @RequestHeader HttpHeaders headers) {
        log.debug("GET findAll()");
        ApiResponse<List<UserDtoResponse>> apiResponse = service.findAll(from, size, headers);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }
}
