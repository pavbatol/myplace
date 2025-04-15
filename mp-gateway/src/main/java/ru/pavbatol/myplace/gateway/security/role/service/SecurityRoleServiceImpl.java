package ru.pavbatol.myplace.gateway.security.role.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.pavbatol.myplace.gateway.app.api.ApiResponse;
import ru.pavbatol.myplace.gateway.app.api.ResponseHandler;
import ru.pavbatol.myplace.gateway.security.role.client.SecurityRoleClient;
import ru.pavbatol.myplace.shared.dto.security.role.RoleDto;

import java.util.List;

/**
 * Service implementation for role management operations.
 * Provides business logic for retrieving role information from the security system.
 *
 * <p><b>Key functionality:</b>
 * <ul>
 *   <li>Retrieve role details by ID</li>
 *   <li>List all available roles in the system</li>
 * </ul>
 *
 * <p><b>Security context:</b> All operations typically require administrative privileges.</p>
 *
 * <p>Delegates to {@link SecurityRoleClient} for communication with the role service API
 * and uses {@link ResponseHandler} for standardized response formatting.</p>
 *
 * @see SecurityRoleService
 * @see SecurityRoleClient
 */
@Service
@RequiredArgsConstructor
public class SecurityRoleServiceImpl implements SecurityRoleService {
    private final SecurityRoleClient client;
    private final ResponseHandler responseHandler;

    @Override
    public ApiResponse<RoleDto> findById(Long roleId, HttpHeaders headers) {
        ResponseEntity<Object> response = client.findById(roleId, headers);
        return responseHandler.processResponse(response, RoleDto.class);
    }

    @Override
    public ApiResponse<List<RoleDto>> findAll(HttpHeaders headers) {
        ResponseEntity<Object> response = client.findAll(headers);
        return responseHandler.processResponseList(response, RoleDto.class);
    }
}
