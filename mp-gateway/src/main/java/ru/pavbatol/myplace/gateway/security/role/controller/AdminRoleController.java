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

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/${app.mp.security.label}/admin/roles")
@Tag(name = "Admin: Role", description = "API for working with roles")
public class AdminRoleController {

    private final SecurityRoleService service;

    @GetMapping("/{roleId}")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "findById", description = "getting a role by Id")
    public ResponseEntity<ApiResponse<RoleDto>> findById(@PathVariable("roleId") Long roleId,
                                                         @RequestHeader HttpHeaders headers) {
        log.debug("GET findById() with roleId {}", roleId);
        ApiResponse<RoleDto> apiResponse = service.findById(roleId, headers);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    @GetMapping
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "findAll", description = "getting roles")
    public ResponseEntity<ApiResponse<List<RoleDto>>> findAll(@RequestHeader HttpHeaders headers) {
        log.debug("GET findAll()");
        ApiResponse<List<RoleDto>> apiResponse = service.findAll(headers);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }
}
