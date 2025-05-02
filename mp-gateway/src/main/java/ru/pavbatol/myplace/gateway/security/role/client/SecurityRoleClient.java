package ru.pavbatol.myplace.gateway.security.role.client;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import ru.pavbatol.myplace.gateway.security.role.service.SecurityRoleService;

/**
 * REST client interface for Role Management Service operations.
 * Provides low-level communication with the role service API.
 *
 * <p><b>Key Features:</b>
 * <ul>
 *   <li>Role retrieval by ID</li>
 *   <li>Complete role listing</li>
 * </ul>
 *
 * <p><b>Usage Notes:</b>
 * <ul>
 *   <li>All operations require admin privileges</li>
 *   <li>Returns raw ResponseEntity - response parsing is the caller's responsibility</li>
 *   <li>Expects JWT authentication token in HttpHeaders</li>
 * </ul>
 *
 * @see SecurityRoleService The higher-level service interface
 * @see ResponseEntity For response structure details
 */
public interface SecurityRoleClient {
    ResponseEntity<Object> findById(Long roleId, HttpHeaders headers);

    ResponseEntity<Object> findAll(HttpHeaders headers);
}
