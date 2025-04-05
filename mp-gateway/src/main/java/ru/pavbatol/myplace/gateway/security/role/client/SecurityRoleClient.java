package ru.pavbatol.myplace.gateway.security.role.client;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import ru.pavbatol.myplace.gateway.app.api.ApiResponse;
import ru.pavbatol.myplace.shared.dto.security.role.RoleDto;

import java.util.List;

public interface SecurityRoleClient {
    ResponseEntity<ApiResponse<RoleDto>> findById(Long roleId, HttpHeaders headers);

    ResponseEntity<ApiResponse<List<RoleDto>>> findAll(HttpHeaders headers);
}
