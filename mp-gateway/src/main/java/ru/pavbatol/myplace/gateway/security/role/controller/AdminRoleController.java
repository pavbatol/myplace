package ru.pavbatol.myplace.gateway.security.role.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.pavbatol.myplace.gateway.app.api.ApiResponse;
import ru.pavbatol.myplace.gateway.security.role.service.SecurityRoleService;
import ru.pavbatol.myplace.shared.dto.security.role.RoleDto;

import java.util.List;

/**
 * Administrative controller for role management operations.
 * Provides endpoints for retrieving role information from the system.
 *
 * <p><b>Key features:</b>
 * <ul>
 *   <li>Retrieve single role by ID</li>
 *   <li>List all available roles</li>
 * </ul>
 *
 * <p><b>Security:</b> All endpoints require JWT authentication with administrator privileges.</p>
 * <p><b>Base Path:</b> {@code ${api.prefix}/${app.mp.security.label}/admin/roles}</p>
 *
 * @see SecurityRoleService
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/${app.mp.security.label}/admin/roles")
@Tag(name = "Admin: Role", description = "API for working with roles")
public class AdminRoleController {

    private final SecurityRoleService service;

    /**
     * Retrieves a specific role by its unique identifier.
     *
     * @param roleId  the ID of the role to retrieve
     * @param headers HTTP headers containing authorization tokens
     * @return response containing the requested role details
     */
    @GetMapping("/{roleId}")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "findById", description = "getting a role by Id")
    public ResponseEntity<ApiResponse<RoleDto>> findById(@PathVariable("roleId") Long roleId,
                                                         @RequestHeader HttpHeaders headers) {
        log.debug("GET findById() with roleId {}", roleId);
        ApiResponse<RoleDto> apiResponse = service.findById(roleId, headers);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    /**
     * Retrieves a list of all available roles in the system.
     *
     * @param headers HTTP headers containing authorization tokens
     * @return response containing list of all roles
     */
    @GetMapping
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "findAll", description = "getting roles")
    public ResponseEntity<ApiResponse<List<RoleDto>>> findAll(@RequestHeader HttpHeaders headers) {
        log.debug("GET findAll()");
        ApiResponse<List<RoleDto>> apiResponse = service.findAll(headers);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }
}
