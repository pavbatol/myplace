package ru.pavbatol.myplace.gateway.security.role.service;

import org.springframework.http.HttpHeaders;
import ru.pavbatol.myplace.gateway.app.api.ApiResponse;
import ru.pavbatol.myplace.shared.dto.security.role.RoleDto;

import java.util.List;

public interface SecurityRoleService {
    ApiResponse<RoleDto> findById(Long roleId, HttpHeaders headers);

    ApiResponse<List<RoleDto>> findAll(HttpHeaders headers);
}
