package ru.pavbatol.myplace.gateway.security.role.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.pavbatol.myplace.gateway.app.api.ApiResponse;
import ru.pavbatol.myplace.gateway.app.util.HttpUtils;
import ru.pavbatol.myplace.gateway.security.role.client.SecurityRoleClient;
import ru.pavbatol.myplace.shared.dto.security.role.RoleDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/${app.mp.security.label}/admin/roles")
@Tag(name = "Admin: Role", description = "API for working with roles")
public class AdminRoleController {

    private final SecurityRoleClient client;

    @GetMapping("/{roleId}")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "findById", description = "getting a role by Id")
    public ResponseEntity<ApiResponse<RoleDto>> findById(@PathVariable("roleId") Long roleId,
                                                         HttpServletRequest servletRequest) {
        log.debug("GET findById() with roleId {}", roleId);
        return client.findById(roleId, HttpUtils.extractHeaders(servletRequest));
    }

    @GetMapping
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "findAll", description = "getting roles")
    public ResponseEntity<ApiResponse<List<RoleDto>>> findAll(HttpServletRequest servletRequest) {
        log.debug("GET findAll()");
        return client.findAll(HttpUtils.extractHeaders(servletRequest));
    }
}
