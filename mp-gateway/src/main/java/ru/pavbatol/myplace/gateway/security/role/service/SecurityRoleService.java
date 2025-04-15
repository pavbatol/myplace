package ru.pavbatol.myplace.gateway.security.role.service;

import org.springframework.http.HttpHeaders;
import ru.pavbatol.myplace.gateway.app.api.ApiResponse;
import ru.pavbatol.myplace.shared.dto.security.role.RoleDto;

import java.util.List;

/**
 * Service interface for role management operations.
 * Provides business-level access to role information with standardized responses.
 *
 * <p><b>Core Functionality:</b>
 * <ul>
 *   <li>Retrieve individual role details</li>
 *   <li>List all available roles in the system</li>
 * </ul>
 *
 * <p><b>Security Requirements:</b>
 * <ul>
 *   <li>All operations require admin privileges</li>
 *   <li>JWT authentication token must be provided in headers</li>
 * </ul>
 *
 * <p>Returns wrapped {@link ApiResponse} with:
 * <ul>
 *   <li>Success: {@link RoleDto} or {@code List<RoleDto>}</li>
 *   <li>Failure: Appropriate error code and message</li>
 * </ul>
 *
 * @see ru.pavbatol.myplace.gateway.security.role.client.SecurityRoleClient The underlying REST client implementation
 * @see RoleDto Role data transfer object
 */
public interface SecurityRoleService {
    ApiResponse<RoleDto> findById(Long roleId, HttpHeaders headers);

    ApiResponse<List<RoleDto>> findAll(HttpHeaders headers);
}
