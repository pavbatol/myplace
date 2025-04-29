package ru.pavbatol.myplace.security.role.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.pavbatol.myplace.security.role.service.RoleService;
import ru.pavbatol.myplace.shared.dto.security.role.RoleDto;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/admin/roles")
@Tag(name = "Admin: Role", description = "API for working with roles")
public class AdminRoleController {

    private final RoleService roleService;

    @GetMapping("/{roleId}")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "findById", description = "getting a role by Id")
    public ResponseEntity<RoleDto> findById(@PathVariable("roleId") Long roleId) {
        log.debug("GET findById() with roleId {}", roleId);
        RoleDto body = roleService.findById(roleId);
        return ResponseEntity.ok(body);
    }

    @GetMapping
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "findAll", description = "getting roles")
    public ResponseEntity<List<RoleDto>> findAll() {
        log.debug("GET findAll()");
        List<RoleDto> body = roleService.findAll();
        return ResponseEntity.ok(body);
    }
}
